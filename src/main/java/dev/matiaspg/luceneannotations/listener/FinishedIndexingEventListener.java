package dev.matiaspg.luceneannotations.listener;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import dev.matiaspg.luceneannotations.event.FinishedIndexingEvent;
import dev.matiaspg.luceneannotations.lucene.SearchableIndex;
import dev.matiaspg.luceneannotations.lucene.SearchableIndexContainer;
import dev.matiaspg.luceneannotations.model.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinishedIndexingEventListener {
    private final SearchableIndexContainer searchIndexContainer;

    @EventListener
    void onApplicationEvent(FinishedIndexingEvent event) throws IOException {
        log.info("Using the new index");

        Directory directory = event.getDirectory();
        Analyzer analyzer = event.getAnalyzer();

        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        SearchableIndex searchableIndex = SearchableIndex.builder()
                .directory(directory)
                .analyzer(analyzer)
                .searcher(searcher)
                .build();

        searchIndexContainer.setFor(Article.class, searchableIndex);
    }
}
