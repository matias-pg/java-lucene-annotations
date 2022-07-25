package dev.matiaspg.luceneannotations.lucene.fieldindexer;

import org.apache.lucene.document.Document;

import java.lang.reflect.Field;

public interface FieldIndexer<T> {
    /**
     * Checks if this indexer supports a {@code Field} type. If it's true, it
     * means it is safe to cast the {@code Field} value as {@code T}.
     *
     * @param fieldType The {@code Field} type to check
     * @return {@code true} if this indexer can index a {@code Field} type
     */
    boolean supports(Class<?> fieldType);

    /**
     * Indexes the {@code Field} in a Lucene {@code Document}.
     *
     * @param field {@code Field} to be indexed
     * @param value Value of the field
     * @param doc   {@code Document} where the {@code Field} will be indexed
     */
    void index(Field field, T value, Document doc);
}
