package dev.matiaspg.luceneannotations.lucene.fieldindexer;

import dev.matiaspg.luceneannotations.lucene.annotation.Sorted;
import dev.matiaspg.luceneannotations.lucene.annotation.Stored;
import org.apache.lucene.document.Document;

import java.lang.reflect.Field;

public interface FieldIndexer<T> {
    Class<T> supportedType();

    /**
     * Checks if this indexer supports a {@code Field} type. If it's true, it
     * means it is safe to cast the {@code Field} value as {@code T}.
     *
     * @param fieldType The {@code Field} type to check
     * @return {@code true} if this indexer can index a {@code Field} type
     */
    default boolean supports(Class<?> fieldType) {
        return supportedType().isAssignableFrom(fieldType);
    }

    /**
     * Indexes the {@code Field} in a Lucene {@code Document}.
     *
     * @param field {@code Field} to be indexed
     * @param value Value of the field
     * @param doc   {@code Document} where the {@code Field} will be indexed
     */
    void index(Field field, T value, Document doc);

    // TODO: Create a default method for isIndexed?

    default boolean isSorted(Field field) {
        return field.isAnnotationPresent(Sorted.class);
    }

    default boolean isStored(Field field) {
        return field.isAnnotationPresent(Stored.class);
    }
}
