package dev.matiaspg.luceneannotations.lucene.fieldindexer;

import dev.matiaspg.luceneannotations.lucene.annotation.Stored;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StoredField;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

@Component
public class IntegerFieldIndexer implements FieldIndexer<Integer> {
    @Override
    public boolean supports(Class<?> fieldType) {
        return Integer.class.isAssignableFrom(fieldType);
    }

    @Override
    public void index(Field field, Integer value, Document doc) {
        boolean isStored = field.isAnnotationPresent(Stored.class);

        doc.add(new LongPoint(field.getName(), value));
        if (isStored) {
            doc.add(new StoredField(field.getName(), Objects.toString(value)));
        }
    }
}
