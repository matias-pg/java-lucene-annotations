package dev.matiaspg.luceneannotations.lucene.fieldindexer;

import org.apache.lucene.document.BinaryDocValuesField;
import org.apache.lucene.document.Document;
import org.apache.lucene.util.BytesRef;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * Example class, not used currently.
 */
@Component
public class ByteArrayFieldIndexer implements FieldIndexer<byte[]> {
    @Override
    public Class<byte[]> supportedType() {
        return byte[].class;
    }

    @Override
    public void index(Field field, byte[] value, Document doc) {
        doc.add(new BinaryDocValuesField(field.getName(), new BytesRef(value)));
    }
}
