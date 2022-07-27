package dev.matiaspg.luceneannotations.lucene.fieldindexer;

import dev.matiaspg.luceneannotations.lucene.annotation.Sorted;
import dev.matiaspg.luceneannotations.lucene.annotation.Stored;
import org.apache.lucene.document.Document;

import java.lang.reflect.Field;

public interface FieldIndexer<T> {
    Class<T> supportedType();

    /**
     * Checks if this indexer supports a {@link Field} type. If it's true, it
     * means it is safe to cast the {@link Field} value as {@link T}.
     *
     * @param fieldType The {@link Field} type to check
     * @return {@link true} if this indexer can index a {@link Field} type
     */
    default boolean supports(Class<?> fieldType) {
        return supportedType().isAssignableFrom(fieldType);
    }

    /**
     * Indexes the {@link Field} in a Lucene {@link Document}.
     *
     * @param field {@link Field} to be indexed
     * @param value Value of the field
     * @param doc   {@link Document} where the {@link Field} will be indexed
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
