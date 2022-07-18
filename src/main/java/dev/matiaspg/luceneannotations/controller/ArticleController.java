package dev.matiaspg.luceneannotations.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.matiaspg.luceneannotations.model.Article;
import dev.matiaspg.luceneannotations.service.ArticleService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService service;

    @GetMapping("/search")
    public List<Article> search(@RequestParam("q") String term, Pageable pageable) {
        return service.search(term, pageable);
    }
}
