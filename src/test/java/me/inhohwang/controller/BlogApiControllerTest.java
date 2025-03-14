package me.inhohwang.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.inhohwang.dto.AddArticleRequest;
import me.inhohwang.dto.UpdateArticleRequest;
import me.inhohwang.repository.BlogRepository;
import me.inhohwang.repository.UserRepository;
import me.inhohwang.springbootdeveloper.domain.Article;
import me.inhohwang.springbootdeveloper.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
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
    @Autowired
    private UserRepository userRepository;
    User user;
    @BeforeEach
    public void mockSetup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        blogRepository.deleteAll();
    }
    @BeforeEach
    void setSecurityContext(){
        userRepository.deleteAll();
        user=userRepository.save(User.builder().email("user@gmail.com").password("test").build());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));

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

        Principal principal= Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");
        //when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(principal)
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
        Article savedArticle=createDefaultAricle();

       //when
        final ResultActions resultActions=mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(savedArticle.getContent()))
                .andExpect((jsonPath("$[0].title").value(savedArticle.getTitle())));

    }

    private Article createDefaultAricle() {
        return blogRepository.save(Article.builder()
                .title("title")
                .content("content")
                .author(user.getUsername())
                .build());
    }

    @DisplayName("findById : 블로그 글 조회에 성공한다.")
    @Test
    public void findById() throws Exception {
        //given
        final String url="/api/articles/{id}";
        Article savedArticle=createDefaultAricle();
        //when
        final ResultActions resultActions=mockMvc.perform(get(url,savedArticle.getId()));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(savedArticle.getContent()))
                .andExpect((jsonPath("$.title").value(savedArticle.getTitle())));
    }
    @DisplayName("delete Atricle : 블로그 글 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception {
        //given
        final String url="/api/articles/{id}";
        Article savedArticle=createDefaultAricle();;
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
        Article savedArticle=createDefaultAricle();

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