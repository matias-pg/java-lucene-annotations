package dev.matiaspg.luceneannotations.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import dev.matiaspg.luceneannotations.lucene.annotation.Indexed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id
    @GeneratedValue
    @Indexed
    // @Stored
    private Integer id;

    @Indexed
    // @Sorted
    private String title;

    @Column(length = 1000)
    @Indexed
    private String content;
}
