package dev.matiaspg.luceneannotations.lucene;

import dev.matiaspg.luceneannotations.lucene.fieldindexer.FieldIndexer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class FieldIndexersContainer {
    private final Collection<FieldIndexer<?>> fieldIndexers;

    // TODO: Cache the indexers for each type

    /**
     * Gets a {@link FieldIndexer} for a specific type.
     *
     * @param fieldType The field type for which an indexer will be retrieved
     * @param <T>       The field type
     * @return The {@link FieldIndexer}
     */
    public <T> FieldIndexer<T> getFor(Class<T> fieldType) {
        // TODO: Check an alternative that doesn't require suppressing

        // The cast is safe, since `supports()` guarantees compatibility
        @SuppressWarnings("unchecked")
        FieldIndexer<T> indexer = (FieldIndexer<T>) fieldIndexers
                .stream()
                .filter(r -> r.supports(fieldType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not find any indexer for field of type \""
                        + fieldType.getName() + "\""));

        return indexer;
    }
}
