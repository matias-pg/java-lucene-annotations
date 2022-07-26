package dev.matiaspg.luceneannotations.event;

import lombok.Data;

import java.util.List;

/**
 * Generic event that is published when a {@code List} of indexable items is
 * loaded and ready to be indexed.
 *
 * @param <T> The type of the items
 */
@Data(staticConstructor = "of")
public class LoadedIndexableItemsEvent<T> {
    /**
     * This is only needed to maintain the type at runtime, since it is
     * available only at compile time.
     * <p>
     * We could also get the type from the first item of the list, but it would
     * not work if the list we've got is empty. Maybe YAGNI?
     */
    private final Class<T> targetClass;

    private final List<T> items;
}
