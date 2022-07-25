package dev.matiaspg.luceneannotations.lucene.fieldindexer;

import dev.matiaspg.luceneannotations.lucene.annotation.Stored;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FloatDocValuesField;
import org.apache.lucene.document.StoredField;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

@Component
public class FloatFieldIndexer implements FieldIndexer<Float> {
    @Override
    public boolean supports(Class<?> fieldType) {
        return Float.class.isAssignableFrom(fieldType);
    }

    @Override
    public void index(Field field, Float value, Document doc) {
        boolean isStored = field.isAnnotationPresent(Stored.class);

        doc.add(new FloatDocValuesField(field.getName(), value));
        if (isStored) {
            doc.add(new StoredField(field.getName(), Objects.toString(value)));
        }
    }
}
