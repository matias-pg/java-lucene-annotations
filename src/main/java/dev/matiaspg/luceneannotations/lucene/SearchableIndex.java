package dev.matiaspg.luceneannotations.lucene;

import lombok.Builder;
import lombok.Data;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;

@Data
@Builder
public class SearchableIndex<T> {
    private Analyzer analyzer;
    private Directory directory;
    private IndexSearcher searcher;
    private final Class<T> itemType;
}
