package me.inhohwang.service;

import lombok.RequiredArgsConstructor;
import me.inhohwang.dto.AddArticleRequest;
import me.inhohwang.dto.UpdateArticleRequest;
import me.inhohwang.repository.BlogRepository;
import me.inhohwang.springbootdeveloper.domain.Article;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor //final이 붙거나 @Notnull이 붙은 필드의 생성자 추가
@Service
public class BlogService {

    private final BlogRepository blogRepository;
    public Article save(AddArticleRequest request, String author) {
        return blogRepository.save(request.toEntity(author));
    }
    public List<Article> findAll() {
        return blogRepository.findAll();
    }
    public Article findById(Long id) {
        return blogRepository.findById(id).orElseThrow(()->new IllegalArgumentException("not found: " +id));
    }
    public void delete(Long id) {
       Article article = blogRepository.findById(id).orElseThrow(()->new IllegalArgumentException("not found: " +id));
        authotizeArticleAuthor(article);
        blogRepository.delete(article);
    }
    @Transactional
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id).orElseThrow(()->new IllegalArgumentException("not found: " +id));
        authotizeArticleAuthor(article);
        article.update(request.getTitle(), request.getContent());
        return article;

    }
    private static void authotizeArticleAuthor(Article article) {
        String userName= SecurityContextHolder.getContext().getAuthentication().getName();
        if(!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }
}
