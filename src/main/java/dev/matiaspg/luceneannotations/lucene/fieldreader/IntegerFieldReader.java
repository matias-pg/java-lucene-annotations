package dev.matiaspg.luceneannotations.lucene.fieldreader;

import org.apache.lucene.index.IndexableField;
import org.springframework.stereotype.Component;

@Component
public class IntegerFieldReader implements FieldReader<Integer> {
    @Override
    public boolean supports(Class<?> fieldType) {
        return Integer.class.isAssignableFrom(fieldType);
    }

    @Override
    public Integer read(IndexableField indexed) {
        return Integer.valueOf(indexed.stringValue());
    }
}
