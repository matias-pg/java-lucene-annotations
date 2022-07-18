package dev.matiaspg.luceneannotations.listener;

import static dev.matiaspg.luceneannotations.utils.LuceneWriteUtils.createDocumentFrom;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import dev.matiaspg.luceneannotations.event.FinishedIndexingEvent;
import dev.matiaspg.luceneannotations.event.IndexChunkCreatedEvent;
import dev.matiaspg.luceneannotations.lucene.WritableIndex;
import dev.matiaspg.luceneannotations.model.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class IndexChunkCreatedEventListener {
    private final ApplicationEventPublisher publisher;

    @Async
    @EventListener
    void onApplicationEvent(IndexChunkCreatedEvent event) throws IOException {
        // TODO: Make the event generic
        @SuppressWarnings("unchecked")
        List<Article> articles = (List<Article>) event.getChunk();

        log.info("Indexing chunk {} of {} with {} articles",
                event.getChunkIndex() + 1, event.getTotalChunks(), articles.size());

        WritableIndex writableIndex = event.getWritableIndex();
        IndexWriter writer = writableIndex.getWriter();

        for (Article article : articles) {
            Document doc = createDocumentFrom(article);
            writer.addDocument(doc);
        }

        // If there are no remaining chunks
        if (event.getRemainingCount().decrementAndGet() == 0) {
            log.info("Writing index in chunk {}", event.getChunkIndex() + 1);

            writer.close();

            log.info("Finished writing index");

            FinishedIndexingEvent finishedIndexingEvent = FinishedIndexingEvent.builder()
                    .directory(writableIndex.getDirectory())
                    .analyzer(writableIndex.getAnalyzer())
                    .build();

            publisher.publishEvent(finishedIndexingEvent);
        }
    }
}
