package dev.matiaspg.luceneannotations.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

public interface SearchService {
    <T> List<T> search(Class<T> targetClass, String term, Pageable pageable);
}
