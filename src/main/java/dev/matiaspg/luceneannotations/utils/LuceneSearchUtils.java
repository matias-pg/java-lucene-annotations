package dev.matiaspg.luceneannotations.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldCollector;
import org.springframework.data.domain.Pageable;

import dev.matiaspg.luceneannotations.lucene.annotation.Indexed;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class LuceneSearchUtils {
    private final String SORT_FIELD_SUFFIX = "--sort";

    public Field[] getAnnotatedFields(Class<?> targetClass) {
        return Arrays.stream(targetClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Indexed.class))
                .toArray(Field[]::new);
    }

    public String[] getAnnotatedFieldNames(Class<?> targetClass) {
        return Arrays.stream(getAnnotatedFields(targetClass))
                .map(Field::getName)
                .toArray(String[]::new);
    }

    public Query createQueryFor(String term, String[] fields, Analyzer analyzer) throws ParseException {
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer);

        parser.setAllowLeadingWildcard(true);

        return parser.parse("*" + QueryParserBase.escape(term) + "*");
    }

    public TopDocs topDocsFor(Query query, Pageable pageable, IndexSearcher searcher) throws IOException {
        int offset = pageable.getPageNumber() * pageable.getPageNumber();

        Sort sort = createSortFrom(pageable);

        TopFieldCollector collector = TopFieldCollector
                .create(sort, offset + pageable.getPageSize(), Integer.MAX_VALUE);

        searcher.search(query, collector);

        return collector.topDocs(offset, pageable.getPageSize());
    }

    private Sort createSortFrom(Pageable pageable) {
        var sort = pageable.getSort();
        if (sort.isUnsorted()) {
            return null;
        }

        SortField[] fields = sort.stream()
                .map(order -> new SortField(
                        order.getProperty() + SORT_FIELD_SUFFIX,
                        SortField.Type.STRING,
                        order.getDirection().isDescending()))
                .toArray(SortField[]::new);

        return new Sort(fields);
    }

    public <T> List<T> asListOf(Class<T> targetClass, TopDocs topDocs, IndexSearcher searcher) {
        return Arrays.stream(topDocs.scoreDocs)
                .map(scoreDoc -> {
                    try {
                        Document doc = searcher.doc(scoreDoc.doc);
                        return asInstanceOf(targetClass, doc);
                    } catch (Exception e) {
                        log.error("Couldn't map to target class", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private <T> T asInstanceOf(Class<T> targetClass, Document doc)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        T item = targetClass.getDeclaredConstructor().newInstance();

        for (IndexableField field : doc.getFields()) {
            try {
                Field classField = targetClass.getDeclaredField(field.name());
                classField.setAccessible(true);
                classField.set(item, getValue(field, classField));
            } catch (NoSuchFieldException e) {
                log.warn("Class \"{}\" doesn't have the field \"{}\"", targetClass.getName(), field.name());
                continue;
            }
        }

        return item;
    }

    // TODO: Improve this method
    private Object getValue(IndexableField indexed, Field classField) {
        Class<?> fieldType = classField.getType();

        if (CharSequence.class.isAssignableFrom(fieldType)) {
            return indexed.getCharSequenceValue();
        }
        try {
            if (Integer.class.isAssignableFrom(fieldType)) {
                return Integer.valueOf(indexed.stringValue());
            }
            if (Long.class.isAssignableFrom(fieldType)) {
                return Long.valueOf(indexed.stringValue());
            }
            if (Float.class.isAssignableFrom(fieldType)) {
                return Float.valueOf(indexed.stringValue());
            }
            if (Double.class.isAssignableFrom(fieldType)) {
                return Double.valueOf(indexed.stringValue());
            }
            if (Number.class.isAssignableFrom(fieldType)) {
                return NumberFormat.getInstance().parse(indexed.stringValue());
            }
        } catch (java.text.ParseException | NumberFormatException e) {
            return null;
        }
        return indexed.binaryValue();
    }

    /*
     * public String getIdFieldOf(Class<?> targetClass) {
     * return Arrays.stream(targetClass.getDeclaredFields())
     * .filter(field -> field.isAnnotationPresent(Id.class))
     * .findFirst()
     * .get()
     * .getName();
     * }
     */
}
