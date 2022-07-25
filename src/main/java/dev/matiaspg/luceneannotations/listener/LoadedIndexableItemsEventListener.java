package dev.matiaspg.luceneannotations.listener;

import dev.matiaspg.luceneannotations.event.IndexChunkCreatedEvent;
import dev.matiaspg.luceneannotations.event.LoadedIndexableItemsEvent;
import dev.matiaspg.luceneannotations.lucene.WritableIndex;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoadedIndexableItemsEventListener {
    private static final int CHUNK_SIZE = 10_000;

    private final ApplicationEventPublisher publisher;

    @EventListener
    <T> void onApplicationEvent(LoadedIndexableItemsEvent<T> event) throws IOException {
        WritableIndex writableIndex = createWritableIndex();

        log.info("Indexing {} items in chunks of {}", event.getItems().size(), CHUNK_SIZE);

        // Split the items in chunks, so they can be indexed concurrently very fast
        List<List<T>> itemChunks = chunked(event.getItems(), CHUNK_SIZE);
        int totalChunks = itemChunks.size();

        // Used to track when we reach the last index
        AtomicInteger remainingCount = new AtomicInteger(totalChunks);

        // This is only used to keep track of the current chunk in logs
        int chunkNumber = 1;

        for (List<T> chunk : itemChunks) {
            // Create an event for the current chunk
            IndexChunkCreatedEvent<T> indexChunkCreatedEvent = IndexChunkCreatedEvent.<T>builder()
                    .writableIndex(writableIndex)
                    .chunk(chunk)
                    .chunkNumber(chunkNumber++)
                    .totalChunks(totalChunks)
                    .remainingCount(remainingCount)
                    .targetClass(event.getTargetClass())
                    .build();

            // Publish the event, which should be processed concurrently
            publisher.publishEvent(indexChunkCreatedEvent);
        }
    }

    private WritableIndex createWritableIndex() throws IOException {
        Directory directory = new ByteBuffersDirectory();
        Analyzer analyzer = new StandardAnalyzer();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(directory, config);

        return WritableIndex.builder()
                .directory(directory)
                .analyzer(analyzer)
                .writer(writer)
                .build();
    }

    private <T> List<List<T>> chunked(List<T> list, int size) {
        List<List<T>> chunked = new ArrayList<>();

        for (int i = 0; i < list.size(); i += size) {
            int end = Math.min(list.size(), i + size);
            chunked.add(list.subList(i, end));
        }

        return chunked;
    }
}
