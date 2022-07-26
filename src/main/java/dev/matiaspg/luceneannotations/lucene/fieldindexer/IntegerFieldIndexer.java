package dev.matiaspg.luceneannotations.lucene.fieldindexer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StoredField;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

@Component
public class IntegerFieldIndexer implements FieldIndexer<Integer> {
    @Override
    public Class<Integer> supportedType() {
        return Integer.class;
    }

    @Override
    public void index(Field field, Integer value, Document doc) {
        doc.add(new LongPoint(field.getName(), value));

        if (isStored(field)) {
            doc.add(new StoredField(field.getName(), Objects.toString(value)));
        }
    }
}
