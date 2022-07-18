package dev.matiaspg.luceneannotations.lucene;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class SearchableIndexContainer {
    private Map<Class<?>, SearchableIndex> indexes = new HashMap<>();

    public SearchableIndex getFor(Class<?> targetClass) {
        return indexes.get(targetClass);
    }

    public void setFor(Class<?> targetClass, SearchableIndex index) {
        indexes.put(targetClass, index);
    }

    public void removeFor(Class<?> targetClass) {
        indexes.remove(targetClass);
    }
}
