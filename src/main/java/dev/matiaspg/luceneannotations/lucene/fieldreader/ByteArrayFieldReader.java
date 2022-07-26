package dev.matiaspg.luceneannotations.lucene.fieldreader;

import org.apache.lucene.index.IndexableField;
import org.springframework.stereotype.Component;

@Component
public class ByteArrayFieldReader implements FieldReader<byte[]> {
    @Override
    public Class<byte[]> supportedType() {
        return byte[].class;
    }

    @Override
    public byte[] read(IndexableField indexed) {
        return indexed.binaryValue().bytes;
    }
}
