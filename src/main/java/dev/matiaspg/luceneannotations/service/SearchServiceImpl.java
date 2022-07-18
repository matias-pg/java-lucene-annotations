package dev.matiaspg.luceneannotations.service;

import static dev.matiaspg.luceneannotations.utils.LuceneSearchUtils.asListOf;
import static dev.matiaspg.luceneannotations.utils.LuceneSearchUtils.createQueryFor;
import static dev.matiaspg.luceneannotations.utils.LuceneSearchUtils.getAnnotatedFieldNames;
import static dev.matiaspg.luceneannotations.utils.LuceneSearchUtils.topDocsFor;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import dev.matiaspg.luceneannotations.exception.BadRequestException;
import dev.matiaspg.luceneannotations.lucene.SearchableIndex;
import dev.matiaspg.luceneannotations.lucene.SearchableIndexContainer;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final SearchableIndexContainer indexesContainer;

    @Override
    public <T> List<T> search(Class<T> targetClass, String term, Pageable pageable) {
        SearchableIndex index = indexesContainer.getFor(targetClass);

        Assert.notNull(index, "No index was found for \"" + targetClass.getSimpleName() + "\"");

        String[] fields = getAnnotatedFieldNames(targetClass);

        try {
            Query query = createQueryFor(term, fields, index.getAnalyzer());

            IndexSearcher searcher = index.getSearcher();

            TopDocs topDocs = topDocsFor(query, pageable, searcher);

            return asListOf(targetClass, topDocs, searcher);
        } catch (ParseException | IOException e) {
            throw new BadRequestException("Invalid search term", e);
        }
    }
}
