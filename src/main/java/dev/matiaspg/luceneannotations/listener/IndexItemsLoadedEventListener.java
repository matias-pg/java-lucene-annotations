package dev.matiaspg.luceneannotations.listener;

import dev.matiaspg.luceneannotations.event.IndexChunkCreatedEvent;
import dev.matiaspg.luceneannotations.event.IndexItemsLoadedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class IndexItemsLoadedEventListener {
    private static final int CHUNK_SIZE = 10_000;

    private final ApplicationEventPublisher publisher;

    @EventListener
    <T> void onApplicationEvent(IndexItemsLoadedEvent<T> event) {
        log.info("Indexing {} items in chunks of {}", event.getItems().size(), CHUNK_SIZE);

        // Split the items in chunks, so they can be indexed concurrently very fast
        List<List<T>> itemChunks = chunked(event.getItems());
        int totalChunks = itemChunks.size();

        // Used to track when we reach the last index
        AtomicInteger remainingCount = new AtomicInteger(totalChunks);

        // This is only used to keep track of the current chunk in logs
        int chunkNumber = 1;

        for (List<T> chunk : itemChunks) {
            // Create an event for the current chunk
            IndexChunkCreatedEvent<T> indexChunkCreatedEvent = IndexChunkCreatedEvent.<T>builder()
                    .writableIndex(event.getWritableIndex())
                    .chunk(chunk)
                    .chunkNumber(chunkNumber++)
                    .totalChunks(totalChunks)
                    .remainingCount(remainingCount)
                    .itemType(event.getItemType())
                    .indexId(event.getIndexId())
                    .build();

            // Publish the event, which should be processed concurrently
            publisher.publishEvent(indexChunkCreatedEvent);
        }
    }

    private <T> List<List<T>> chunked(List<T> list) {
        List<List<T>> chunked = new ArrayList<>();

        for (int i = 0; i < list.size(); i += CHUNK_SIZE) {
            int end = Math.min(list.size(), i + CHUNK_SIZE);
            chunked.add(list.subList(i, end));
        }

        return chunked;
    }
}
