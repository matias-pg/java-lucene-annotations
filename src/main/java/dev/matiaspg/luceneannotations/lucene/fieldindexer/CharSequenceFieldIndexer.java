package dev.matiaspg.luceneannotations.lucene.fieldindexer;

import dev.matiaspg.luceneannotations.lucene.annotation.Stored;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

@Component
public class CharSequenceFieldIndexer implements FieldIndexer<CharSequence> {
    @Override
    public boolean supports(Class<?> fieldType) {
        return CharSequence.class.isAssignableFrom(fieldType);
    }

    @Override
    public void index(Field field, CharSequence value, Document doc) {
        boolean isStored = field.isAnnotationPresent(Stored.class);

        doc.add(new TextField(field.getName(), Objects.toString(value), isStored ? Store.YES : Store.NO));
    }
}
