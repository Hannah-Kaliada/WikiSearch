package com.search.wiki.controller_tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.search.wiki.controller.ArticleController;
import com.search.wiki.entity.Article;
import com.search.wiki.entity.Query;
import com.search.wiki.exceptions.customexceptions.NotFoundException;
import com.search.wiki.service.ArticleService;
import com.search.wiki.service.WikipediaApiService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ArticleControllerTest {

    @Mock
    private ArticleService articleService;

    @Mock
    private WikipediaApiService wikipediaApiService;

    @InjectMocks
    private ArticleController articleController;
    @Captor
    private ArgumentCaptor<Query> queryCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindAllArticles() {
        // Mock data
        List<Article> mockArticles = new ArrayList<>();
        Article article1 = new Article();
        article1.setId(1L);
        article1.setTitle("Title 1");
        mockArticles.add(article1);

        Article article2 = new Article();
        article2.setId(2L);
        article2.setTitle("Title 2");
        mockArticles.add(article2);

        // Mock service method
        when(articleService.findAllArticles()).thenReturn(mockArticles);

        // Call controller method
        ResponseEntity<List<Article>> responseEntity = articleController.findAllArticles();

        // Assertions
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, responseEntity.getBody().size());
    }

    @Test
    void testFindById() {
        long articleId = 1L;
        Article mockArticle = new Article();
        mockArticle.setId(articleId);
        mockArticle.setTitle("Title");

        when(articleService.findById(articleId)).thenReturn(mockArticle);

        ResponseEntity<Article> responseEntity = articleController.findById(articleId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockArticle, responseEntity.getBody());
    }
    @Test
    void testFindByIdNotFound() {
        long nonExistingId = 999L;

        when(articleService.findById(nonExistingId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            articleController.findById(nonExistingId);
        });
    }

    @Test
    void testDeleteArticle() {
        long articleId = 1L;

        when(articleService.deleteArticle(articleId)).thenReturn(true);

        ResponseEntity<String> responseEntity = articleController.deleteArticle(articleId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Article deleted successfully!", responseEntity.getBody());
    }

    @Test
    void testSaveArticle() {
        Article articleToSave = new Article();
        articleToSave.setTitle("New Article");

    // Mock behavior of articleService.saveArticle() to return the saved article
    when(articleService.saveArticle(articleToSave)).thenReturn(articleToSave);

    // Call controller method
    ResponseEntity<String> responseEntity = articleController.saveArticle(articleToSave);

    // Verify the response
    assertNotNull(responseEntity);
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    assertEquals("Article saved successfully!", responseEntity.getBody());

    // Verify that saveArticle() was called with the expected argument
    verify(articleService, times(1)).saveArticle(articleToSave);

    // Verify that wikipediaApiService.search() was called with the expected argument
    ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
    verify(wikipediaApiService, times(1)).search(queryCaptor.capture());

    // Assert the captured argument (Query object) passed to wikipediaApiService.search()
    Query capturedQuery = queryCaptor.getValue();
    assertNotNull(capturedQuery);
    assertEquals(articleToSave.getTitle(), capturedQuery.getSearchTerm());
    }

    @Test
    void testUpdateArticle() {
        long articleId = 1L;
        Article existingArticle = new Article();
        existingArticle.setId(articleId);
        existingArticle.setTitle("Existing Article");
        existingArticle.setImagePath("");


        Article updatedArticle = new Article();
        updatedArticle.setId(articleId);
        updatedArticle.setTitle("Updated Article");

        when(articleService.updateArticle(updatedArticle, articleId)).thenReturn(updatedArticle);

        ResponseEntity<Article> responseEntity = articleController.updateArticle(updatedArticle, articleId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedArticle, responseEntity.getBody());
    }

    @Test
    void testUpdateArticleNotFound() {
        long nonExistingId = 999L;
        Article updatedArticle = new Article();
        updatedArticle.setId(nonExistingId);
        updatedArticle.setTitle("Updated Article");

        when(articleService.updateArticle(updatedArticle, nonExistingId)).thenReturn(null);

        ResponseEntity<Article> responseEntity = articleController.updateArticle(updatedArticle, nonExistingId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void testGetTop5ArticlesByUserCount() {
        List<Article> top5Articles = new ArrayList<>();
        // Populate top5Articles with mock data

        when(articleService.findTop5ArticlesByUserCount()).thenReturn(top5Articles);

        ResponseEntity<List<Article>> responseEntity = articleController.getTop5ArticlesByUserCount();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(top5Articles, responseEntity.getBody());
    }

    @Test
    void testSearchArticlesByKeyword() {
        String keyword = "searchKeyword";
        List<Article> searchResults = new ArrayList<>();
        // Populate searchResults with mock data

        when(articleService.searchArticlesByKeyword(keyword)).thenReturn(searchResults);

        List<Article> result = articleController.searchArticles(keyword);

        assertNotNull(result);
        assertEquals(searchResults.size(), result.size());
        // You can add more detailed assertions if needed
    }
    @Test
    void testDeleteArticleNotFound() {
        // Prepare data for non-existing article
        long nonExistingId = 999L;

        // Mock service method to return false (article not found)
        when(articleService.deleteArticle(nonExistingId)).thenReturn(false);

        // Call controller method and expect NotFoundException to be thrown
        assertThrows(NotFoundException.class, () -> {
            articleController.deleteArticle(nonExistingId);
        });

        // Verify that deleteArticle() was called with the expected argument
        verify(articleService, times(1)).deleteArticle(nonExistingId);
    }
}
