package dev.matiaspg.luceneannotations.service;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchService {
    List<?> search(String indexId, String searchTerm, Pageable pageable);
}
