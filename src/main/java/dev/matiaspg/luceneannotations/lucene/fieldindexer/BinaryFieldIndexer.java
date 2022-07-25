package dev.matiaspg.luceneannotations.lucene.fieldindexer;

import org.apache.lucene.document.BinaryDocValuesField;
import org.apache.lucene.document.Document;
import org.apache.lucene.util.BytesRef;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class BinaryFieldIndexer implements FieldIndexer<byte[]> {
    @Override
    public boolean supports(Class<?> fieldType) {
        return byte[].class.isAssignableFrom(fieldType);
    }

    @Override
    public void index(Field field, byte[] value, Document doc) {
        doc.add(new BinaryDocValuesField(field.getName(), new BytesRef(value)));
    }
}
