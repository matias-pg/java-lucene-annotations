package dev.matiaspg.luceneannotations.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import dev.matiaspg.luceneannotations.model.Article;

public interface ArticleService {
    List<Article> findAll();

    List<Article> search(String term, Pageable pageable);

    Article findById(String id);

    Article save(Article article);

    List<Article> saveAll(Iterable<Article> articles);

    void update(String id, Article article);

    void delete(String id);
}
