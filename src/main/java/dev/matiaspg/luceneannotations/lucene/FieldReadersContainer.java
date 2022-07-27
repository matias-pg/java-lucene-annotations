package dev.matiaspg.luceneannotations.lucene;

import dev.matiaspg.luceneannotations.lucene.fieldreader.FieldReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class FieldReadersContainer {
    private final Collection<FieldReader<?>> fieldReaders;

    // TODO: Cache the readers for each type

    /**
     * Gets a {@link FieldReader} for a specific type.
     *
     * @param fieldType The field type for which a reader will be retrieved
     * @param <T>       The field type
     * @return The {@link FieldReader}
     */
    public <T> FieldReader<T> getFor(Class<T> fieldType) {
        // TODO: Check an alternative that doesn't require suppressing

        // The cast is safe, since `supports()` guarantees compatibility
        @SuppressWarnings("unchecked")
        FieldReader<T> reader = (FieldReader<T>) fieldReaders
                .stream()
                .filter(r -> r.supports(fieldType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not find any field reader for field of type \""
                        + fieldType.getName() + "\""));

        return reader;
    }
}
