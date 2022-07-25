package dev.matiaspg.luceneannotations.model;

import dev.matiaspg.luceneannotations.lucene.annotation.Indexed;
import dev.matiaspg.luceneannotations.lucene.annotation.Sorted;
import dev.matiaspg.luceneannotations.lucene.annotation.Stored;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id
    @GeneratedValue
    @Indexed
    @Stored
    private String id;

    @Indexed
    @Stored
    @Sorted
    private String title;

    @Indexed
    @Stored
    // @Sorted
    private String author;

    @Column(length = 1000)
    // @Indexed
    @Stored
    private String url;

    @Stored
    private Long points;

    @Stored
    private Long numberOfComments;

    @Indexed
    @Stored
    @Sorted
    private Date createdAt;
}
