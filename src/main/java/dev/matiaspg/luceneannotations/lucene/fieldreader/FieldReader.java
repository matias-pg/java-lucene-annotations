package dev.matiaspg.luceneannotations.lucene.fieldreader;

import org.apache.lucene.index.IndexableField;

import java.lang.reflect.Field;

public interface FieldReader<T> {
    Class<T> supportedType();

    /**
     * Checks if this reader supports a {@link Field} type. If it's true, it
     * means it is safe to cast the {@link IndexableField} value as {@link T}.
     *
     * @param fieldType The {@link Field} type to check
     * @return {@link true} if this reader can read a {@link Field} type
     */
    default boolean supports(Class<?> fieldType) {
        return supportedType().isAssignableFrom(fieldType);
    }

    /**
     * Reads an {@link IndexableField} and returns the value before it was
     * indexed.
     *
     * @param indexed {@link IndexableField} to be read
     */
    T read(IndexableField indexed);
}
