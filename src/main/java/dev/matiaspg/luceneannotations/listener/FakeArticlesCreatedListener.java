package dev.matiaspg.luceneannotations.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import dev.matiaspg.luceneannotations.event.FakeArticlesCreatedEvent;
import dev.matiaspg.luceneannotations.event.IndexChunkCreatedEvent;
import dev.matiaspg.luceneannotations.lucene.WritableIndex;
import dev.matiaspg.luceneannotations.model.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FakeArticlesCreatedListener {
    private final ApplicationEventPublisher publisher;

    @EventListener
    void onApplicationEvent(FakeArticlesCreatedEvent event) throws IOException {
        List<Article> articles = event.getArticles();

        Directory directory = new ByteBuffersDirectory();
        Analyzer analyzer = new StandardAnalyzer();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(directory, config);

        WritableIndex writableIndex = WritableIndex.builder()
                .directory(directory)
                .analyzer(analyzer)
                .writer(writer)
                .build();

        int chunkSize = 10_000;
        List<List<Article>> articleChunks = chunked(articles, chunkSize);
        int totalChunks = articleChunks.size();
        AtomicInteger remainingCount = new AtomicInteger(totalChunks);

        log.info("Indexing {} articles in chunks of {}", articles.size(), chunkSize);

        int chunkIndex = 0;
        for (List<Article> chunk : articleChunks) {
            IndexChunkCreatedEvent indexChunkCreatedEvent = IndexChunkCreatedEvent.builder()
                    .writableIndex(writableIndex)
                    .chunk(chunk)
                    .chunkIndex(chunkIndex++)
                    .totalChunks(totalChunks)
                    .remainingCount(remainingCount)
                    .build();

            publisher.publishEvent(indexChunkCreatedEvent);
        }
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
