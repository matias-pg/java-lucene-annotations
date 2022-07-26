package dev.matiaspg.luceneannotations.lucene.fieldreader;

import org.apache.lucene.index.IndexableField;
import org.springframework.stereotype.Component;

@Component
public class CharSequenceFieldReader implements FieldReader<CharSequence> {
    @Override
    public Class<CharSequence> supportedType() {
        return CharSequence.class;
    }

    @Override
    public CharSequence read(IndexableField indexed) {
        return indexed.getCharSequenceValue();
    }
}
