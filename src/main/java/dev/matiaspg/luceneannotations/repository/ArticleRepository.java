package dev.matiaspg.luceneannotations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.matiaspg.luceneannotations.model.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {
}
