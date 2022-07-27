package dev.matiaspg.luceneannotations.event;

import dev.matiaspg.luceneannotations.lucene.WritableIndex;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder
public class IndexChunkCreatedEvent<T> {
    private final List<T> chunk;
    private final Class<T> itemType;

    private final int chunkNumber;
    private final int totalChunks;
    private final AtomicInteger remainingCount;

    private final String indexId;
    private final WritableIndex writableIndex;
}
