package dev.matiaspg.luceneannotations.utils;

import static dev.matiaspg.luceneannotations.utils.LuceneSearchUtils.getAnnotatedFields;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.lucene.document.BinaryDocValuesField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.util.BytesRef;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LuceneWriteUtils {
    public <T> Document createDocumentFrom(T object) {
        Document doc = new Document();

        Field[] indexedFields = getAnnotatedFields(object.getClass());

        for (Field field : indexedFields) {
            field.setAccessible(true);
            try {
                List<IndexableField> indexableFields = fieldsFor(field, object);
                indexableFields.forEach(doc::add);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                // Shouldn't happen, but if it happens just ignore the field
            }
        }

        return doc;
    }

    // TODO: Improve this method
    private List<IndexableField> fieldsFor(Field field, Object object)
            throws IllegalArgumentException, IllegalAccessException {
        List<IndexableField> fields = new ArrayList<>();

        String name = field.getName();
        Object value = field.get(object);

        if (Number.class.isAssignableFrom(field.getType())) {
            fields.add(new LongPoint(name, (Integer) value));
            fields.add(new StoredField(name, Objects.toString(value)));
        } else if (CharSequence.class.isAssignableFrom(field.getType())) {
            fields.add(new TextField(name, Objects.toString(value), Store.YES));
        } else {
            fields.add(new BinaryDocValuesField(name, new BytesRef(Objects.toString(value))));
        }

        return fields;
    }
}
