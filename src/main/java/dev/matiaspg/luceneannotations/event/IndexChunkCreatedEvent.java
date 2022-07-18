package dev.matiaspg.luceneannotations.event;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import dev.matiaspg.luceneannotations.lucene.WritableIndex;
import lombok.Builder;
import lombok.Data;

/**
 * TODO: Make it generic
 */
@Data
@Builder
public class IndexChunkCreatedEvent {
    private final List<?> chunk;
    private final int chunkIndex;
    private final int totalChunks;
    private final AtomicInteger remainingCount;
    private final WritableIndex writableIndex;
}
