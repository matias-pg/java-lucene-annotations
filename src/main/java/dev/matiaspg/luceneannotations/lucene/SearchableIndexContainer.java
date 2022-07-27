package dev.matiaspg.luceneannotations.lucene;

import dev.matiaspg.luceneannotations.exception.IndexNotAvailableException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that contains all {@link SearchableIndex}es that are available at the
 * moment.
 * <p>
 * Note that is has the limitation
 */
@Component
public class SearchableIndexContainer {
    private final Map<String, SearchableIndex<?>> indexes = new HashMap<>();

    public SearchableIndex<?> get(String indexId) {
        SearchableIndex<?> found = indexes.get(indexId);

        if (found == null) {
            throw new IndexNotAvailableException("No index was found for \"" + indexId + "\"");
        }

        return found;
    }

    public void put(String indexId, SearchableIndex<?> searchableIndex) {
        indexes.put(indexId, searchableIndex);
    }
}
