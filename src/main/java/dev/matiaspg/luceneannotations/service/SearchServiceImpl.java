package dev.matiaspg.luceneannotations.service;

import dev.matiaspg.luceneannotations.exception.BadRequestException;
import dev.matiaspg.luceneannotations.lucene.FieldReadersContainer;
import dev.matiaspg.luceneannotations.lucene.SearchableIndex;
import dev.matiaspg.luceneannotations.lucene.SearchableIndexContainer;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static dev.matiaspg.luceneannotations.utils.LuceneSearchUtils.*;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final SearchableIndexContainer indexesContainer;
    private final FieldReadersContainer fieldReadersContainer;

    @Override
    public <T> List<T> search(Class<T> targetClass, String term, Pageable pageable) {
        SearchableIndex index = indexesContainer.getFor(targetClass);

        String[] fields = getAnnotatedFieldNames(targetClass);

        try {
            Query query = createQueryFor(term, fields, index.getAnalyzer());

            IndexSearcher searcher = index.getSearcher();

            TopDocs topDocs = topDocsFor(query, pageable, searcher);

            return asListOf(targetClass, topDocs, searcher, fieldReadersContainer);
        } catch (ParseException | IOException e) {
            throw new BadRequestException("Invalid search term", e);
        }
    }
}
