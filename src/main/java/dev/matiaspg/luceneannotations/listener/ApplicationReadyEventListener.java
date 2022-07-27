package dev.matiaspg.luceneannotations.listener;

import dev.matiaspg.luceneannotations.event.IndexItemsLoadedEvent;
import dev.matiaspg.luceneannotations.lucene.WritableIndex;
import dev.matiaspg.luceneannotations.model.Article;
import dev.matiaspg.luceneannotations.utils.CsvLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Component
public class ApplicationReadyEventListener {
    @EventListener(ApplicationReadyEvent.class)
    IndexItemsLoadedEvent<Article> onApplicationEvent() throws IOException {
        log.info("Loading articles from Hacker News (using a local CSV)");

        long start = System.currentTimeMillis();
        List<Article> articles = loadArticles();
        long end = System.currentTimeMillis();

        log.info("Loaded {} articles, took {} ms", articles.size(), end - start);

        WritableIndex writableIndex = createWritableIndex();

        return IndexItemsLoadedEvent.<Article>builder()
                .itemType(Article.class)
                .items(articles)
                .indexId("articles")
                .writableIndex(writableIndex)
                .build();
    }

    /**
     * Load articles from a local CSV.
     */
    private List<Article> loadArticles() throws IOException {
        InputStream stream = new ClassPathResource("data/stories_1000000.csv").getInputStream();
        InputStreamReader reader = new InputStreamReader(stream);

        // Get all CSVRecords from the CSV file
        Iterable<CSVRecord> articleIterable = CsvLoader.load(reader);

        // Map each CSVRecord to an Article
        return StreamSupport.stream(articleIterable.spliterator(), true)
                .map(this::mapArticle)
                .collect(Collectors.toList());
    }

    private Article mapArticle(CSVRecord record) {
        return Article.builder()
                .id(record.get("ID"))
                .title(record.get("Title"))
                .author(record.get("Author"))
                .url(record.get("URL"))
                .points(Long.parseLong(record.get("Points")))
                .numberOfComments(Long.parseLong(record.get("Number of Comments")))
                .createdAt(Date.from(Instant.parse(record.get("Created At"))))
                .build();
    }

    /**
     * Create the WritableIndex where the articles will be indexed.
     */
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
}
