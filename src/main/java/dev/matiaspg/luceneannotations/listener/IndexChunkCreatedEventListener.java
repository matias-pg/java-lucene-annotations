package dev.matiaspg.luceneannotations.listener;

import dev.matiaspg.luceneannotations.event.FinishedIndexingEvent;
import dev.matiaspg.luceneannotations.event.IndexChunkCreatedEvent;
import dev.matiaspg.luceneannotations.lucene.FieldIndexersContainer;
import dev.matiaspg.luceneannotations.lucene.WritableIndex;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static dev.matiaspg.luceneannotations.utils.LuceneWriteUtils.createDocumentFrom;

@Slf4j
@Component
@RequiredArgsConstructor
public class IndexChunkCreatedEventListener {
    private final ApplicationEventPublisher publisher;

    private final FieldIndexersContainer fieldIndexersContainer;

    @Async
    @EventListener
    <T> void onApplicationEvent(IndexChunkCreatedEvent<T> event) throws IOException {
        List<T> items = event.getChunk();

        log.info("Indexing chunk {} of {} with {} items",
                event.getChunkNumber(), event.getTotalChunks(), items.size());

        WritableIndex writableIndex = event.getWritableIndex();
        IndexWriter writer = writableIndex.getWriter();

        // Map each item into a Lucene Document
        for (T item : items) {
            Document doc = createDocumentFrom(item, fieldIndexersContainer);
            writer.addDocument(doc);
        }

        // If there are no remaining chunks
        if (event.getRemainingCount().decrementAndGet() == 0) {
            log.info("Writing index in chunk {}", event.getChunkNumber());

            // Commit the changes and close the IndexWriter
            writer.close();

            log.info("Finished writing index");

            // Create an event to communicate that the indexing process finished
            FinishedIndexingEvent<T> finishedIndexingEvent = FinishedIndexingEvent.<T>builder()
                    .directory(writableIndex.getDirectory())
                    .analyzer(writableIndex.getAnalyzer())
                    .targetClass(event.getTargetClass())
                    .build();

            // Publish the event
            publisher.publishEvent(finishedIndexingEvent);
        }
    }
}
