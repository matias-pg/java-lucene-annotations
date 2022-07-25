package dev.matiaspg.luceneannotations.event;

import dev.matiaspg.luceneannotations.model.Article;

import java.util.List;

public class LoadedArticlesEvent extends LoadedIndexableItemsEvent<Article> {
    public LoadedArticlesEvent(List<Article> items) {
        super(items, Article.class);
    }
}
