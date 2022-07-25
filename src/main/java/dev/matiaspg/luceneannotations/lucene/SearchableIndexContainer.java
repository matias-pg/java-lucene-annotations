package dev.matiaspg.luceneannotations.lucene;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class SearchableIndexContainer {
    private final Map<Class<?>, SearchableIndex> indexes = new HashMap<>();

    public SearchableIndex getFor(Class<?> targetClass) {
        return Objects.requireNonNull(
                indexes.get(targetClass),
                "No index was found for \"" + targetClass.getSimpleName() + "\""
        );
    }

    public void setFor(Class<?> targetClass, SearchableIndex index) {
        indexes.put(targetClass, index);
    }
}
