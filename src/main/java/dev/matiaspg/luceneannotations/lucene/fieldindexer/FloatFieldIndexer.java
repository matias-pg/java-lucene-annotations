package dev.matiaspg.luceneannotations.lucene.fieldindexer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.FloatDocValuesField;
import org.apache.lucene.document.StoredField;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

@Component
public class FloatFieldIndexer implements FieldIndexer<Float> {
    @Override
    public Class<Float> supportedType() {
        return Float.class;
    }

    @Override
    public void index(Field field, Float value, Document doc) {
        doc.add(new FloatDocValuesField(field.getName(), value));

        if (isStored(field)) {
            doc.add(new StoredField(field.getName(), Objects.toString(value)));
        }
    }
}
