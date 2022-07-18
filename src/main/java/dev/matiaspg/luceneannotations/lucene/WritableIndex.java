package dev.matiaspg.luceneannotations.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WritableIndex {
    private Analyzer analyzer;
    private Directory directory;
    private IndexWriter writer;
}
