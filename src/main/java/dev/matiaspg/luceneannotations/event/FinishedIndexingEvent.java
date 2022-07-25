package dev.matiaspg.luceneannotations.event;

import lombok.Builder;
import lombok.Data;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;

@Data
@Builder
public class FinishedIndexingEvent<T> {
    private final Directory directory;
    private final Analyzer analyzer;

    /**
     * @see LoadedIndexableItemsEvent#getTargetClass()
     */
    private final Class<T> targetClass;
}
