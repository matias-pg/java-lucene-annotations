package dev.matiaspg.luceneannotations.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchableIndex {
    private Analyzer analyzer;
    private Directory directory;
    private IndexSearcher searcher;
}
