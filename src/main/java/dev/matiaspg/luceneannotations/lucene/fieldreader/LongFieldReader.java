package dev.matiaspg.luceneannotations.lucene.fieldreader;

import org.apache.lucene.index.IndexableField;
import org.springframework.stereotype.Component;

@Component
public class LongFieldReader implements FieldReader<Long> {
    @Override
    public boolean supports(Class<?> fieldType) {
        return Long.class.isAssignableFrom(fieldType);
    }

    @Override
    public Long read(IndexableField indexed) {
        return Long.valueOf(indexed.stringValue());
    }
}
