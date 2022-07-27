package dev.matiaspg.luceneannotations.lucene.fieldindexer;

import dev.matiaspg.luceneannotations.lucene.annotation.Sorted;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.util.BytesRef;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

@Component
public class CharSequenceFieldIndexer implements FieldIndexer<CharSequence> {
    @Override
    public Class<CharSequence> supportedType() {
        return CharSequence.class;
    }

    @Override
    public void index(Field field, CharSequence value, Document doc) {
        Store stored = isStored(field) ? Store.YES : Store.NO;

        doc.add(new TextField(field.getName(), Objects.toString(value), stored));

        if (isSorted(field)) {
            var sortedFieldName = field.getName() + Sorted.SORT_FIELD_SUFFIX;

            doc.add(new SortedDocValuesField(sortedFieldName, new BytesRef(value)));
        }
    }
}
