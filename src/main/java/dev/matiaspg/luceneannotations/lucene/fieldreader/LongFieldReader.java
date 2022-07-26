package dev.matiaspg.luceneannotations.lucene.fieldreader;

import org.apache.lucene.index.IndexableField;
import org.springframework.stereotype.Component;

@Component
public class LongFieldReader implements FieldReader<Long> {
    @Override
    public Class<Long> supportedType() {
        return Long.class;
    }

    @Override
    public Long read(IndexableField indexed) {
        return Long.valueOf(indexed.stringValue());
    }
}
