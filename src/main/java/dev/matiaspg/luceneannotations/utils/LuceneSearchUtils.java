package dev.matiaspg.luceneannotations.utils;

import dev.matiaspg.luceneannotations.lucene.FieldReadersContainer;
import dev.matiaspg.luceneannotations.lucene.annotation.Indexed;
import dev.matiaspg.luceneannotations.lucene.annotation.Sorted;
import dev.matiaspg.luceneannotations.lucene.annotation.Stored;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.*;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class LuceneSearchUtils {
    public static Field[] getAnnotatedFields(Class<?> targetClass) {
        return Arrays.stream(targetClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Indexed.class)
                        || field.isAnnotationPresent(Stored.class)
                        || field.isAnnotationPresent(Sorted.class))
                .toArray(Field[]::new);
    }

    public static String[] getAnnotatedFieldNames(Class<?> targetClass) {
        return Arrays.stream(getAnnotatedFields(targetClass))
                .map(Field::getName)
                .toArray(String[]::new);
    }

    public static Query createQueryFor(String term, String[] fields, Analyzer analyzer) throws ParseException {
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer);

        // Important: this causes a huge drop in performance,
        // in exchange for making the search more flexible
        parser.setAllowLeadingWildcard(true);

        return parser.parse("*" + QueryParserBase.escape(term) + "*");
    }

    public static TopDocs topDocsFor(Query query, Pageable pageable, IndexSearcher searcher) throws IOException {
        int offset = pageable.getPageNumber() * pageable.getPageNumber();

        Sort sort = createSortFrom(pageable);

        TopFieldCollector collector = TopFieldCollector
                .create(sort, offset + pageable.getPageSize(), Integer.MAX_VALUE);

        searcher.search(query, collector);

        return collector.topDocs(offset, pageable.getPageSize());
    }

    private static Sort createSortFrom(Pageable pageable) {
        var sort = pageable.getSort();
        if (sort.isUnsorted()) {
            return new Sort();
        }

        SortField[] fields = sort.stream()
                .map(order -> new SortField(
                        order.getProperty() + Sorted.SORT_FIELD_SUFFIX,
                        SortField.Type.STRING,
                        order.getDirection().isDescending()))
                .toArray(SortField[]::new);

        return new Sort(fields);
    }

    public static <T> List<T> asListOf(
            Class<T> targetClass,
            TopDocs topDocs,
            IndexSearcher searcher,
            FieldReadersContainer fieldReadersContainer
    ) {
        return Arrays.stream(topDocs.scoreDocs)
                .map(scoreDoc -> {
                    try {
                        Document doc = searcher.doc(scoreDoc.doc);
                        return asInstanceOf(targetClass, doc, fieldReadersContainer);
                    } catch (Exception e) {
                        log.error("Couldn't map to target class", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Creates an object of type {@code T} using the fields of an indexed
     * {@code Document}.
     *
     * @param targetClass           The class that will be created
     * @param doc                   {@code Document} containing the indexed fields
     * @param fieldReadersContainer Container of {@code FieldReader}s
     * @param <T>                   Type of the object
     * @return The object containing the indexed data
     */
    private static <T> T asInstanceOf(Class<T> targetClass, Document doc, FieldReadersContainer fieldReadersContainer)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // Creates an instance of the target class
        T object = targetClass.getDeclaredConstructor().newInstance();

        // For each indexed field
        for (IndexableField indexedField : doc.getFields()) {
            try {
                // Gets a class field containing the same name as the indexed field
                Field field = targetClass.getDeclaredField(indexedField.name());
                field.setAccessible(true);

                // Get the field value using the indexed data
                var value = fieldReadersContainer.getFor(field.getType()).read(indexedField);

                // Set the field value to the object
                field.set(object, value);
            } catch (NoSuchFieldException e) {
                log.warn("Class \"{}\" doesn't have the field \"{}\"", targetClass.getName(), indexedField.name());
            }
        }

        return object;
    }
}
