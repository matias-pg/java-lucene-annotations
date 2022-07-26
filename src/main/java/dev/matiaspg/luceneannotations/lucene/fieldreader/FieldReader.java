package dev.matiaspg.luceneannotations.lucene.fieldreader;

import org.apache.lucene.index.IndexableField;

public interface FieldReader<T> {
    Class<T> supportedType();

    /**
     * Checks if this reader supports a {@code Field} type. If it's true, it
     * means it is safe to cast the {@code IndexableField} value as {@code T}.
     *
     * @param fieldType The {@code Field} type to check
     * @return {@code true} if this reader can read a {@code Field} type
     */
    default boolean supports(Class<?> fieldType) {
        return supportedType().isAssignableFrom(fieldType);
    }

    /**
     * Reads an {@code IndexableField} and returns the value before it was
     * indexed.
     *
     * @param indexed {@code IndexableField} to be read
     */
    T read(IndexableField indexed);
}
