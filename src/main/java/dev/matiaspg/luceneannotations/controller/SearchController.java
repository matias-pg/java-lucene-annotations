package dev.matiaspg.luceneannotations.controller;

import dev.matiaspg.luceneannotations.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    /**
     * Search items in an index.
     * <p>
     * Useful to search for "blog_articles" or "support_articles", for example.
     */
    @GetMapping("/{indexId}")
    public List<?> search(
            @PathVariable String indexId,
            @RequestParam("q") String searchTerm,
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

        return searchService.search(indexId, searchTerm, sortedPageable);
    }
}
