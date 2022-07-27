package dev.matiaspg.luceneannotations.lucene.fieldindexer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleDocValuesField;
import org.apache.lucene.document.StoredField;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Example class, not used currently.
 */
@Component
public class DoubleFieldIndexer implements FieldIndexer<Double> {
    @Override
    public Class<Double> supportedType() {
        return Double.class;
    }

    @Override
    public void index(Field field, Double value, Document doc) {
        doc.add(new DoubleDocValuesField(field.getName(), value));

        if (isStored(field)) {
            doc.add(new StoredField(field.getName(), Objects.toString(value)));
        }
    }
}
