package dev.matiaspg.luceneannotations.listener;

import static dev.matiaspg.luceneannotations.utils.TextGenerator.generateTitle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import dev.matiaspg.luceneannotations.event.FakeArticlesCreatedEvent;
import dev.matiaspg.luceneannotations.model.Article;
import dev.matiaspg.luceneannotations.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationReadyListener {
    private final ArticleService articleService;

    @EventListener
    FakeArticlesCreatedEvent onApplicationEvent(ApplicationReadyEvent event) throws IOException {
        log.info("Generating fake articles");

        List<Article> articles = new ArrayList<>();

        for (int i = 0; i < 100_000; i++) {
            String title = generateTitle();
            String content = String.join(" ", Collections.nCopies(10, title));

            Article article = Article.builder()
                    .title(i + title + i)
                    .content(i + content + i)
                    .build();

            articles.add(article);
        }

        articleService.saveAll(articles);

        log.info("Generated {} fake articles", articles.size());

        return new FakeArticlesCreatedEvent(articles);
    }
}
