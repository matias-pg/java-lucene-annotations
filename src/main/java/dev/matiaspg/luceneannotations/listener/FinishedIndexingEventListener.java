package dev.matiaspg.luceneannotations.listener;

import dev.matiaspg.luceneannotations.event.FinishedIndexingEvent;
import dev.matiaspg.luceneannotations.lucene.SearchableIndex;
import dev.matiaspg.luceneannotations.lucene.SearchableIndexContainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinishedIndexingEventListener {
    private final SearchableIndexContainer searchIndexContainer;

    @EventListener
    <T> void onApplicationEvent(FinishedIndexingEvent<T> event) throws IOException {
        log.info("Using the new index for future searches");

        // Get the Directory and Analyzer used while indexing
        Directory directory = event.getDirectory();
        Analyzer analyzer = event.getAnalyzer();

        // Create the index reader that will be used in future searches
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        // Create a SearchableIndex
        SearchableIndex<T> searchableIndex = SearchableIndex.<T>builder()
                .directory(directory)
                .analyzer(analyzer)
                .searcher(searcher)
                .itemType(event.getItemType())
                .build();

        // Make the SearchableIndex available for future searches
        searchIndexContainer.put(event.getIndexId(), searchableIndex);
    }
}
