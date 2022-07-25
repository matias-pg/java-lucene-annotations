package dev.matiaspg.luceneannotations.utils;

import dev.matiaspg.luceneannotations.lucene.FieldIndexersContainer;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;

import java.lang.reflect.Field;

import static dev.matiaspg.luceneannotations.utils.LuceneSearchUtils.getAnnotatedFields;

@Slf4j
public class LuceneWriteUtils {
    /**
     * Creates a Lucene {@code Document} using the fields of an object.
     *
     * @param object                 Object whose fields will be indexed
     * @param fieldIndexersContainer Container of {@code FieldIndexer}s
     * @param <T>                    Type of the object
     * @return The {@code Document} containing the object data
     */
    public static <T> Document createDocumentFrom(T object, FieldIndexersContainer fieldIndexersContainer) {
        Document doc = new Document();

        // Get the fields that have our annotations
        Field[] indexedFields = getAnnotatedFields(object.getClass());

        for (Field field : indexedFields) {
            // Make them accessible since they are probably private
            field.setAccessible(true);
            try {
                // Get the value of the field
                var value = field.get(object);

                // Index the field
                index(field, value, doc, fieldIndexersContainer);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                // Shouldn't happen, but if it happens log the error just in case
                log.warn("Couldn't index a field", e);
            }
        }

        return doc;
    }

    private static <T> void index(
            Field field,
            T value,
            Document doc,
            FieldIndexersContainer fieldIndexersContainer
    ) {
        // TODO: Check an alternative that doesn't require suppressing

        @SuppressWarnings("unchecked")
        // Get the type of the field
        Class<T> fieldType = (Class<T>) field.getType();

        // Indexes the field using an indexer for the field type
        fieldIndexersContainer.getFor(fieldType).index(field, value, doc);
    }
}
