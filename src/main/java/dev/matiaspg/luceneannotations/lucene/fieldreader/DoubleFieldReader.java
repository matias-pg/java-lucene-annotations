package dev.matiaspg.luceneannotations.lucene.fieldreader;

import org.apache.lucene.index.IndexableField;
import org.springframework.stereotype.Component;

@Component
public class DoubleFieldReader implements FieldReader<Double> {
    @Override
    public Class<Double> supportedType() {
        return Double.class;
    }

    @Override
    public Double read(IndexableField indexed) {
        return Double.valueOf(indexed.stringValue());
    }
}
