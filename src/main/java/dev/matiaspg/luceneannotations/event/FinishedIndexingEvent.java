package dev.matiaspg.luceneannotations.event;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinishedIndexingEvent {
    private final Directory directory;
    private final Analyzer analyzer;
}
