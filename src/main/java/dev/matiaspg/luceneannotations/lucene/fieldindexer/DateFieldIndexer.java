package dev.matiaspg.luceneannotations.lucene.fieldindexer;

import dev.matiaspg.luceneannotations.lucene.annotation.Sorted;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.util.BytesRef;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;

@Component
public class DateFieldIndexer implements FieldIndexer<Date> {
    @Override
    public Class<Date> supportedType() {
        return Date.class;
    }

    @Override
    public void index(Field field, Date value, Document doc) {
        String stringValue = value.toInstant().toString();

        doc.add(new StoredField(field.getName(), stringValue));

        if (isSorted(field)) {
            var sortedFieldName = field.getName() + Sorted.SORT_FIELD_SUFFIX;

            doc.add(new SortedDocValuesField(sortedFieldName, new BytesRef(stringValue)));
        }
    }
}
