package dev.matiaspg.luceneannotations.lucene.fieldindexer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StoredField;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

@Component
public class LongFieldIndexer implements FieldIndexer<Long> {
    @Override
    public Class<Long> supportedType() {
        return Long.class;
    }

    @Override
    public void index(Field field, Long value, Document doc) {
        doc.add(new LongPoint(field.getName(), value));

        if (isStored(field)) {
            doc.add(new StoredField(field.getName(), Objects.toString(value)));
        }
    }
}
