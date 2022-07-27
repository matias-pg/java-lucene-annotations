package dev.matiaspg.luceneannotations.model;

import dev.matiaspg.luceneannotations.lucene.annotation.Indexed;
import dev.matiaspg.luceneannotations.lucene.annotation.Sorted;
import dev.matiaspg.luceneannotations.lucene.annotation.Stored;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Indexed
    @Stored
    private String id;

    @Indexed
    @Sorted
    @Stored
    private String title;

    @Indexed
    @Sorted
    @Stored
    private String author;

    @Indexed
    @Sorted
    @Stored
    private String url;

    @Indexed
    @Sorted
    @Stored
    private Long points;

    @Indexed
    @Sorted
    @Stored
    private Long numberOfComments;

    @Indexed
    @Sorted
    @Stored
    private Date createdAt;
}
