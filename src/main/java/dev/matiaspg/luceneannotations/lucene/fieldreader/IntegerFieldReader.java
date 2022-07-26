package dev.matiaspg.luceneannotations.lucene.fieldreader;

import org.apache.lucene.index.IndexableField;
import org.springframework.stereotype.Component;

@Component
public class IntegerFieldReader implements FieldReader<Integer> {
    @Override
    public Class<Integer> supportedType() {
        return Integer.class;
    }

    @Override
    public Integer read(IndexableField indexed) {
        return Integer.valueOf(indexed.stringValue());
    }
}
