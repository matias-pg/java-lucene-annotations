package dev.matiaspg.luceneannotations.lucene.fieldreader;

import org.apache.lucene.index.IndexableField;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class DateFieldReader implements FieldReader<Date> {
    @Override
    public boolean supports(Class<?> fieldType) {
        return Date.class.isAssignableFrom(fieldType);
    }

    @Override
    public Date read(IndexableField indexed) {
        return Date.from(Instant.parse(indexed.stringValue()));
    }
}
