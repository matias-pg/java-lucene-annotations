package dev.matiaspg.luceneannotations.event;

import dev.matiaspg.luceneannotations.lucene.WritableIndex;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Generic event that is published when a {@link List} of indexable items is
 * loaded and ready to be indexed.
 *
 * @param <T> The type of the items
 */
@Data
@Builder
public class IndexItemsLoadedEvent<T> {
    /**
     * This is so we know the type of the items that will be indexed, since
     * Java doesn't maintain the generic type at runtime.
     * <p>
     * We could also use the type of the first item in the list, but it would
     * not work if the list is empty. Maybe YAGNI?
     */
    private final Class<T> itemType;

    private final List<T> items;
    private final String indexId;
    private final WritableIndex writableIndex;
}
