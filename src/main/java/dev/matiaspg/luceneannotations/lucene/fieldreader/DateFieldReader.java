package dev.matiaspg.luceneannotations.lucene.fieldreader;

import org.apache.lucene.index.IndexableField;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class DateFieldReader implements FieldReader<Date> {
    @Override
    public Class<Date> supportedType() {
        return Date.class;
    }

    @Override
    public Date read(IndexableField indexed) {
        return Date.from(Instant.parse(indexed.stringValue()));
    }
}
