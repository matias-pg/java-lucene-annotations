package dev.matiaspg.luceneannotations.lucene.fieldreader;

import org.apache.lucene.index.IndexableField;
import org.springframework.stereotype.Component;

@Component
public class BinaryFieldReader implements FieldReader<byte[]> {
    @Override
    public boolean supports(Class<?> fieldType) {
        return byte[].class.isAssignableFrom(fieldType);
    }

    @Override
    public byte[] read(IndexableField indexed) {
        return indexed.binaryValue().bytes;
    }
}
