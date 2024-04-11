package com.search.wiki.service_tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.search.wiki.cache.Cache;
import com.search.wiki.entity.Article;
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

  @Mock private ArticleRepository articleRepository;

  @Mock private Cache cache;

  @InjectMocks private ArticleService articleService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testSaveArticle_Success() {
    Article inputArticle = new Article();
    inputArticle.setTitle("Test Title");
    inputArticle.setUrl("test-url.com");
    inputArticle.setImagePath("test-image.jpg");

    Article savedArticle = new Article();
    savedArticle.setId(1L);
    savedArticle.setTitle("Test Title");
    savedArticle.setUrl("test-url.com");
    savedArticle.setImagePath("test-image.jpg");

    when(articleRepository.save(any(Article.class))).thenReturn(savedArticle);

    Article result = articleService.saveArticle(inputArticle);

    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("Test Title", result.getTitle());
    assertEquals("test-url.com", result.getUrl());
    assertEquals("test-image.jpg", result.getImagePath());

    verify(cache).put("Article_1", savedArticle);
  }

  @Test
  void testSaveArticle_NullArticle() {
    assertThrows(IllegalArgumentException.class, () -> articleService.saveArticle(null));

    verifyNoInteractions(articleRepository);
    verifyNoInteractions(cache);
  }

  @Test
  void testFindById_ExistingId() {
    long articleId = 1L;
    Article existingArticle = new Article();
    existingArticle.setId(articleId);
    existingArticle.setTitle("Existing Article");
    existingArticle.setUrl("existing-url.com");
    existingArticle.setImagePath("existing-image.jpg");

    when(cache.containsKey("Article_" + articleId)).thenReturn(false);
    when(articleRepository.findById(articleId)).thenReturn(Optional.of(existingArticle));

    Article result = articleService.findById(articleId);

    assertNotNull(result);
    assertEquals(articleId, result.getId());
    assertEquals("Existing Article", result.getTitle());
    assertEquals("existing-url.com", result.getUrl());
    assertEquals("existing-image.jpg", result.getImagePath());

    verify(cache).put("Article_1", existingArticle);
  }

  @Test
  void testFindById_InvalidId() {
    long invalidId = 0L;

    assertThrows(IllegalArgumentException.class, () -> articleService.findById(invalidId));

    verifyNoInteractions(articleRepository);
    verifyNoInteractions(cache);
  }

  @Test
  void testUpdateArticle_Success() {
    long articleId = 1L;
    Article existingArticle = new Article();
    existingArticle.setId(articleId);
    existingArticle.setTitle("Existing Article");
    existingArticle.setUrl("existing-url.com");
    existingArticle.setImagePath("existing-image.jpg");

    Article updatedArticleData = new Article();
    updatedArticleData.setTitle("Updated Article");
    updatedArticleData.setUrl("updated-url.com");
    updatedArticleData.setImagePath("updated-image.jpg");

    when(articleRepository.findById(articleId)).thenReturn(Optional.of(existingArticle));
    when(articleRepository.save(any(Article.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Article updatedArticle = articleService.updateArticle(updatedArticleData, articleId);

    assertNotNull(updatedArticle);
    assertEquals(articleId, updatedArticle.getId());
    assertEquals("Updated Article", updatedArticle.getTitle());
    assertEquals("updated-url.com", updatedArticle.getUrl());
    assertEquals("updated-image.jpg", updatedArticle.getImagePath());

    verify(articleRepository).findById(articleId);
    verify(articleRepository).save(existingArticle);
  }

  @Test
  void testDeleteArticle_Success() {
    long articleId = 1L;
    Article existingArticle = new Article();
    existingArticle.setId(articleId);

    when(articleRepository.findById(articleId)).thenReturn(Optional.of(existingArticle));
    when(articleRepository.existsById(articleId)).thenReturn(false);

    boolean result = articleService.deleteArticle(articleId);

    assertTrue(result);

    verify(articleRepository).findById(articleId);
    verify(articleRepository).deleteById(articleId);
    verify(cache).remove("Article_1");
  }

  @Test
  void testFindTop5ArticlesByUserCount_Success() {
    List<Article> topArticles = new ArrayList<>();
    topArticles.add(new Article("Top Article 1", "top-url1.com", "top-image1.jpg"));
    topArticles.add(new Article("Top Article 2", "top-url2.com", "top-image2.jpg"));

    when(articleRepository.findTop5ArticlesByUserCount(any())).thenReturn(topArticles);

    List<Article> result = articleService.findTop5ArticlesByUserCount();

    assertNotNull(result);
    assertEquals(2, result.size());

    verify(articleRepository).findTop5ArticlesByUserCount(any());
  }

  @Test
  void testSearchArticlesByKeyword_Success() {
    String keyword = "test";

    List<Article> searchResults = new ArrayList<>();
    searchResults.add(new Article("Test Article 1", "test-url1.com", "test-image1.jpg"));
    searchResults.add(new Article("Test Article 2", "test-url2.com", "test-image2.jpg"));

    when(articleRepository.findByTitleContaining(keyword)).thenReturn(searchResults);

    List<Article> result = articleService.searchArticlesByKeyword(keyword);

    assertNotNull(result);
    assertEquals(2, result.size());

    verify(articleRepository).findByTitleContaining(keyword);
  }

  @Test
  void testFindAllArticles_Success() {
    List<Article> articles = new ArrayList<>();

    Article article1 = new Article();
    article1.setId(1L);
    article1.setTitle("Title 1");
    article1.setUrl("url1.com");
    article1.setImagePath("image1.jpg");
    articles.add(article1);

    Article article2 = new Article();
    article2.setId(2L);
    article2.setTitle("Title 2");
    article2.setUrl("url2.com");
    article2.setImagePath("image2.jpg");
    articles.add(article2);

    when(articleRepository.findAll()).thenReturn(articles);

    List<Article> returnedArticles = articleService.findAllArticles();

    assertNotNull(returnedArticles);
    assertFalse(returnedArticles.isEmpty());

    for (Article article : articles) {
      verify(cache).put(getCacheKey(article.getId()), article);
    }
  }

  @Test
  void testFindAllArticles_EmptyList() {
    when(articleRepository.findAll()).thenReturn(new ArrayList<>());

    assertThrows(NotFoundException.class, () -> articleService.findAllArticles());

    verify(cache, never()).put(any(), any());
  }

  private String getCacheKey(Long articleId) {
    return "Article_" + articleId;
  }
}
