package com.search.wiki.service_tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.search.wiki.cache.Cache;
import com.search.wiki.entity.Article;
import com.search.wiki.exceptions.ExceptionConstants;
import com.search.wiki.exceptions.customexceptions.NotFoundException;
import com.search.wiki.repository.ArticleRepository;
import com.search.wiki.service.ArticleService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ArticleServiceTest {

  @Mock private ArticleRepository repository;


  @Mock private Cache cache;

  @InjectMocks private ArticleService articleService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testSaveArticle_Success() {
    Article article = new Article("Test Article", "https://example.com", "image.jpg");
    when(repository.save(any(Article.class))).thenReturn(article);

    Article savedArticle = articleService.saveArticle(article);

    assertNotNull(savedArticle);
    assertEquals(article.getTitle(), savedArticle.getTitle());
    verify(cache).put(anyString(), any(Article.class));
  }

  @Test
  void testFindById_ExistingId_Success() {
    long articleId = 1L;
    Article article = new Article("Test Article", "https://example.com", "image.jpg");
    when(cache.get(anyString())).thenReturn(null);
    when(repository.findById(articleId)).thenReturn(Optional.of(article));

    Article foundArticle = articleService.findById(articleId);

    assertNotNull(foundArticle);
    assertEquals(article.getTitle(), foundArticle.getTitle());
    verify(cache).put(anyString(), any(Article.class));
  }

  @Test
  void testFindById_NonExistingId_ExceptionThrown() {
    long nonExistingId = 999L;
    when(cache.get(anyString())).thenReturn(null);
    when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> articleService.findById(nonExistingId));
  }

  @Test
  void testUpdateArticle_ExistingArticle_Success() {
    long articleId = 1L;
    Article existingArticle = new Article("Existing Article", "https://example.com", "image.jpg");
    Article updatedArticle = new Article("Updated Article", "https://updated.com", "updated.jpg");

    when(repository.findById(articleId)).thenReturn(Optional.of(existingArticle));
    when(repository.save(any(Article.class))).thenReturn(updatedArticle);

    Article result = articleService.updateArticle(updatedArticle, articleId);

    assertNotNull(result);
    assertEquals(updatedArticle.getTitle(), result.getTitle());
    assertEquals(updatedArticle.getUrl(), result.getUrl());
    assertEquals(updatedArticle.getImagePath(), result.getImagePath());
    // verify(cache).put(anyString(), any(Article.class));
  }

  @Test
  void testUpdateArticle_NonExistingArticle_ExceptionThrown() {
    long nonExistingId = 999L;
    Article updatedArticle = new Article("Updated Article", "https://updated.com", "updated.jpg");

    when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class, () -> articleService.updateArticle(updatedArticle, nonExistingId));
  }

  @Test
  void testDeleteArticle_Success() {
    long articleId = 1L;
    Article existingArticle = new Article();
    existingArticle.setId(articleId);

    when(repository.findById(articleId)).thenReturn(Optional.of(existingArticle));
    when(repository.existsById(articleId)).thenReturn(false);

    boolean result = articleService.deleteArticle(articleId);

    assertTrue(result);

    verify(repository).findById(articleId);
    verify(repository).deleteById(articleId);
    verify(cache).remove("Article_1");
  }

  @Test
  void testSaveArticle_NullArticle_ExceptionThrown() {
    // Arrange
    Article nullArticle = null;

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          articleService.saveArticle(nullArticle);
        },
        "Article cannot be null");

    // Verify that repository.save was not called
    verify(repository, never()).save(any(Article.class));

    // Verify that cache.put was not called
    verify(cache, never()).put(anyString(), any(Article.class));
  }

  @Test
  void testDeleteArticle_NonExistingArticle_ExceptionThrown() {
    long nonExistingId = 999L;

    when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> articleService.deleteArticle(nonExistingId));
  }

  @Test
  void testFindAllArticles_Success() {
    List<Article> articles = new ArrayList<>();
    articles.add(new Article("Article 1", "https://example.com", "image1.jpg"));
    articles.add(new Article("Article 2", "https://example.com", "image2.jpg"));

    when(repository.findAll()).thenReturn(articles);

    List<Article> result = articleService.findAllArticles();

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(articles.size(), result.size());
    verify(cache, times(articles.size())).put(anyString(), any(Article.class));
  }

  @Test
  void testFindAllArticles_EmptyList_ExceptionThrown() {
    when(repository.findAll()).thenReturn(new ArrayList<>());

    assertThrows(NotFoundException.class, () -> articleService.findAllArticles());
  }

  @Test
  void testSearchArticlesByKeyword_Success() {
    String keyword = "test";
    List<Article> articles = new ArrayList<>();
    articles.add(new Article("Test Article 1", "https://example.com", "image1.jpg"));
    articles.add(new Article("Test Article 2", "https://example.com", "image2.jpg"));

    when(repository.findByTitleContaining(keyword)).thenReturn(articles);

    List<Article> result = articleService.searchArticlesByKeyword(keyword);

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(articles.size(), result.size());
  }

  @Test
  void testFindById_InvalidId_ExceptionThrown() {
    // Arrange
    long invalidId = -1L;

    // Act and Assert
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              articleService.findById(invalidId);
            },
            "ID must be greater than or equal to 1");

    assertEquals(ExceptionConstants.ID_REQUIRED, exception.getMessage());

    // Verify that repository.findById was never called
    verify(repository, never()).findById(anyLong());

    // Verify that cache.get was never called
    verify(cache, never()).get(anyString());
  }

  @Test
  void testUpdateArticle_InvalidId_ExceptionThrown() {
    // Arrange
    long invalidId = -1L;
    Article updatedArticle = new Article("Updated Article", "https://updated.com", "updated.jpg");

    // Act and Assert
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              articleService.updateArticle(updatedArticle, invalidId);
            },
            "ID must be greater than or equal to 1");

    assertEquals(ExceptionConstants.ID_REQUIRED, exception.getMessage());

    // Verify that repository.findById was never called
    verify(repository, never()).findById(anyLong());

    // Verify that cache.put was never called
    verify(cache, never()).put(anyString(), any(Article.class));
  }

  @Test
  void testDeleteArticle_InvalidId_ExceptionThrown() {
    // Arrange
    long invalidId = -1L;

    // Act and Assert
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> articleService.deleteArticle(invalidId),
            "ID must be greater than or equal to 1");

    assertEquals(ExceptionConstants.ID_REQUIRED, exception.getMessage());

    // Verify that repository.deleteById was never called
    verify(repository, never()).deleteById(anyLong());

    // Verify that cache.remove was never called
    verify(cache, never()).remove(anyString());
  }

  @Test
  void testFindTop5ArticlesByUserCount_Success() {
    List<Article> topArticles = new ArrayList<>();
    topArticles.add(new Article("Top Article 1", "top-url1.com", "top-image1.jpg"));
    topArticles.add(new Article("Top Article 2", "top-url2.com", "top-image2.jpg"));

    when(repository.findTop5ArticlesByUserCount(any())).thenReturn(topArticles);

    List<Article> result = articleService.findTop5ArticlesByUserCount();

    assertNotNull(result);
    assertEquals(2, result.size());

    verify(repository).findTop5ArticlesByUserCount(any());
  }

  @Test
  void testFindTop5ArticlesByUserCount_EmptyList() {
    List<Article> topArticles = new ArrayList<>();
    when(repository.findTop5ArticlesByUserCount(any())).thenReturn(topArticles);
    // Act
    List<Article> result = articleService.findTop5ArticlesByUserCount();

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty()); // Assert that the result list is empty
  }
}
