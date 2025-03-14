package me.inhohwang.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.inhohwang.springbootdeveloper.domain.Article;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
public class ArticleViewResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;

    public ArticleViewResponse(Article article){
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.author = article.getAuthor();
        this.createdAt=article.getCreated_at();

    }
}
