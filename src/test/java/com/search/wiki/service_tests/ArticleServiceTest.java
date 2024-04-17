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

/** The type Article service test. */
class ArticleServiceTest {

  @Mock private ArticleRepository repository;

  @Mock private Cache cache;

  @InjectMocks private ArticleService articleService;

  /** Sets up. */
  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  /** Test save article success. */
  @Test
  void testSaveArticle_Success() {
    Article article = new Article("Test Article", "https://example.com", "image.jpg");
    when(repository.save(any(Article.class))).thenReturn(article);

    Article savedArticle = articleService.saveArticle(article);

    assertNotNull(savedArticle);
    assertEquals(article.getTitle(), savedArticle.getTitle());
    verify(cache).put(anyString(), any(Article.class));
  }

  /** Test find by id existing id success. */
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

  /** Test find by id non existing id exception thrown. */
  @Test
  void testFindById_NonExistingId_ExceptionThrown() {
    long nonExistingId = 999L;
    when(cache.get(anyString())).thenReturn(null);
    when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> articleService.findById(nonExistingId));
  }

  /** Test update article existing article success. */
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
  }

  /** Test update article non existing article exception thrown. */
  @Test
  void testUpdateArticle_NonExistingArticle_ExceptionThrown() {
    long nonExistingId = 999L;
    Article updatedArticle = new Article("Updated Article", "https://updated.com", "updated.jpg");

    when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class, () -> articleService.updateArticle(updatedArticle, nonExistingId));
  }

  /** Test delete article success. */
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

  /** Test save article null article exception thrown. */
  @Test
  void testSaveArticle_NullArticle_ExceptionThrown() {
    Article nullArticle = null;

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          articleService.saveArticle(nullArticle);
        },
        "Article cannot be null");

    verify(repository, never()).save(any(Article.class));

    verify(cache, never()).put(anyString(), any(Article.class));
  }

  /** Test delete article non existing article exception thrown. */
  @Test
  void testDeleteArticle_NonExistingArticle_ExceptionThrown() {
    long nonExistingId = 999L;

    when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> articleService.deleteArticle(nonExistingId));
  }

  /** Test find all articles success. */
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

  /** Test find all articles empty list exception thrown. */
  @Test
  void testFindAllArticles_EmptyList_ExceptionThrown() {
    when(repository.findAll()).thenReturn(new ArrayList<>());

    assertThrows(NotFoundException.class, () -> articleService.findAllArticles());
  }

  /** Test search articles by keyword success. */
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

  /** Test find by id invalid id exception thrown. */
  @Test
  void testFindById_InvalidId_ExceptionThrown() {
    long invalidId = -1L;

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              articleService.findById(invalidId);
            },
            "ID must be greater than or equal to 1");

    assertEquals(ExceptionConstants.ID_REQUIRED, exception.getMessage());

    verify(repository, never()).findById(anyLong());

    verify(cache, never()).get(anyString());
  }

  /** Test update article invalid id exception thrown. */
  @Test
  void testUpdateArticle_InvalidId_ExceptionThrown() {
    long invalidId = -1L;
    Article updatedArticle = new Article("Updated Article", "https://updated.com", "updated.jpg");

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              articleService.updateArticle(updatedArticle, invalidId);
            },
            "ID must be greater than or equal to 1");

    assertEquals(ExceptionConstants.ID_REQUIRED, exception.getMessage());

    verify(repository, never()).findById(anyLong());

    verify(cache, never()).put(anyString(), any(Article.class));
  }

  /** Test delete article invalid id exception thrown. */
  @Test
  void testDeleteArticle_InvalidId_ExceptionThrown() {
    long invalidId = -1L;

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> articleService.deleteArticle(invalidId),
            "ID must be greater than or equal to 1");

    assertEquals(ExceptionConstants.ID_REQUIRED, exception.getMessage());
    verify(repository, never()).deleteById(anyLong());
    verify(cache, never()).remove(anyString());
  }

  /** Test find top 5 articles by user count success. */
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

  /** Test find top 5 articles by user count empty list. */
  @Test
  void testFindTop5ArticlesByUserCount_EmptyList() {
    List<Article> topArticles = new ArrayList<>();
    when(repository.findTop5ArticlesByUserCount(any())).thenReturn(topArticles);
    List<Article> result = articleService.findTop5ArticlesByUserCount();
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}
