package dev.matiaspg.luceneannotations.event;

import lombok.Data;

import java.util.List;

/**
 * Generic event that is published when a {@code List} of indexable items is
 * ready to be indexed.
 * <p>
 * Due to type erasure, I made this class abstract, so that it cannot be
 * published directly. Instead, a class extending this should be published.
 *
 * @param <T> The type of the items
 * @see <a href="https://spring.io/blog/2015/02/11/better-application-events-in-spring-framework-4-2">More info about the type erasure</a>
 */
@Data
public abstract class LoadedIndexableItemsEvent<T> {
    private final List<T> items;

    /**
     * This is only needed to maintain the type at runtime, since it is
     * available only at compile time.
     * <p>
     * We could also get the type from the first item of the list, but it would
     * not work if *for some reason*, the list we've got empty. Maybe YAGNI?
     */
    private final Class<T> targetClass;
}
