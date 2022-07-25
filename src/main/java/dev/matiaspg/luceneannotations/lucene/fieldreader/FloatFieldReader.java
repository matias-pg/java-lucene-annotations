package dev.matiaspg.luceneannotations.lucene.fieldreader;

import org.apache.lucene.index.IndexableField;
import org.springframework.stereotype.Component;

@Component
public class FloatFieldReader implements FieldReader<Float> {
    @Override
    public boolean supports(Class<?> fieldType) {
        return Float.class.isAssignableFrom(fieldType);
    }

    @Override
    public Float read(IndexableField indexed) {
        return Float.valueOf(indexed.stringValue());
    }
}
