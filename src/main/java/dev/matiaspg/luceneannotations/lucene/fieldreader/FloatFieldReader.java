package dev.matiaspg.luceneannotations.lucene.fieldreader;

import org.apache.lucene.index.IndexableField;
import org.springframework.stereotype.Component;

@Component
public class FloatFieldReader implements FieldReader<Float> {
    @Override
    public Class<Float> supportedType() {
        return Float.class;
    }

    @Override
    public Float read(IndexableField indexed) {
        return Float.valueOf(indexed.stringValue());
    }
}
