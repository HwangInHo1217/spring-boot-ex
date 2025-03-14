package me.inhohwang.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    Long id;
    @Column(nullable = false)
    private  String author;
    @Column(nullable = false, name="title")
    String title;
    @Column(nullable = false)
    String content;
    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime created_at;
    @LastModifiedDate
    @Column(name="updated_at")
    private LocalDateTime updated_at;
    @Builder
    public Article(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
