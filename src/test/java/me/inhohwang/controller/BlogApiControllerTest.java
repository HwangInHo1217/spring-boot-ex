package me.inhohwang.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.inhohwang.dto.AddArticleRequest;
import me.inhohwang.dto.UpdateArticleRequest;
import me.inhohwang.repository.BlogRepository;
import me.inhohwang.springbootdeveloper.domain.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected WebApplicationContext webApplicationContext;
    @Autowired
    BlogRepository blogRepository;
    @BeforeEach
    public void mockSetup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        blogRepository.deleteAll();
    }
    @DisplayName("addArticle: 블로그에 글 추가")
    @Test
    public void addArticle() throws Exception {
        //given
        final String url="/api/articles";
        final String title="title";
        final String content="content";
        final AddArticleRequest addArticleRequest=new AddArticleRequest(title,content);

        final String requestBody=objectMapper.writeValueAsString(addArticleRequest);

        //when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        result.andExpect(status().isCreated());
        List<Article> articles=blogRepository.findAll();
        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }
    @DisplayName("findAllArticles : 블로그 글 목록 조회에 성공한다.")
    @Test
    public void findAllArticles() throws Exception {
        final String url="/api/articles";
        final String title="title";
        final String content="content";

        blogRepository.save(Article.builder().title(title).content(content).build());

        //when
        final ResultActions resultActions=mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect((jsonPath("$[0].title").value(title)));

    }
    @DisplayName("findById : 블로그 글 조회에 성공한다.")
    @Test
    public void findById() throws Exception {
        //given
        final String url="/api/articles/{id}";
        final String title="title";
        final String content="content";

        Article savedArticle=blogRepository.save(Article.builder().title(title).content(content).build());
        //when
        final ResultActions resultActions=mockMvc.perform(get(url,savedArticle.getId()));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect((jsonPath("$.title").value(title)));
    }
    @DisplayName("delete Atricle : 블로그 글 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception {
        //given
        final String url="/api/articles/{id}";
        final String title="title";
        final String content="content";
        Article savedArticle=blogRepository.save(Article.builder().title(title).content(content).build());
        //when
        mockMvc.perform(delete(url,savedArticle.getId())).andExpect(status().isOk());
        //then
        List<Article> articles=blogRepository.findAll();
        assertThat(articles).isEmpty();

    }
    @DisplayName("updateArticle : 블로그 글 수정에 성공하기")
    @Test
    public void updateArticle() throws Exception {
        final String url="/api/articles/{id}";
        final String title="title";
        final String content="content";
        Article savedArticle=blogRepository.save(Article.builder().title(title).content(content).build());

        final String newTitle="newTitle";
        final String newContent="newContent";
        UpdateArticleRequest updateArticleRequest = new UpdateArticleRequest(newTitle,newContent);
        //when
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateArticleRequest)));
        //then
        result.andExpect(status().isOk());
        Article article=blogRepository.findById(savedArticle.getId()).get();
        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }
}