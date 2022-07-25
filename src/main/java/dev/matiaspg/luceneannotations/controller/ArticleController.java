package dev.matiaspg.luceneannotations.controller;

import dev.matiaspg.luceneannotations.model.Article;
import dev.matiaspg.luceneannotations.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService service;

    @GetMapping("/search")
    public List<Article> search(
            @RequestParam("q") String term,
            @RequestParam String order,
            Pageable pageable
    ) {
        boolean isDescending = "DESC".equalsIgnoreCase(order);

        // This is to change the direction of the Pageable' Sort, since it is always ASC
        PageRequest sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                isDescending ? pageable.getSort().descending() : pageable.getSort()
        );
        
        return service.search(term, sortedPageable);
    }
}
