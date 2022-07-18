package dev.matiaspg.luceneannotations.event;

import java.util.List;

import dev.matiaspg.luceneannotations.model.Article;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FakeArticlesCreatedEvent {
    private final List<Article> articles;
}
