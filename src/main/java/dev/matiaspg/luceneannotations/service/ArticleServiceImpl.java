package dev.matiaspg.luceneannotations.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.matiaspg.luceneannotations.exception.NotFoundException;
import dev.matiaspg.luceneannotations.model.Article;
import dev.matiaspg.luceneannotations.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository repository;
    private final SearchService searchService;

    @Override
    public List<Article> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Article> search(String term, Pageable pageable) {
        return searchService.search(Article.class, term, pageable);
    }

    @Override
    public Article findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Article not found"));
    }

    @Override
    public Article save(Article article) {
        return repository.save(article);
    }

    @Override
    public List<Article> saveAll(Iterable<Article> articles) {
        return repository.saveAll(articles);
    }

    @Override
    public void update(Integer id, Article article) {
        assertExists(id);
        article.setId(id);
        repository.save(article);
    }

    @Override
    public void delete(Integer id) {
        assertExists(id);
        repository.deleteById(id);
    }

    private void assertExists(Integer id) {
        var exists = repository.existsById(id);
        if (!exists) {
            throw new NotFoundException("Article not found");
        }
    }
}
