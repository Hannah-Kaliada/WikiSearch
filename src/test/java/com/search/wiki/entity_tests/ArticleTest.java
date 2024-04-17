package com.search.wiki.entity_tests;

import static org.junit.jupiter.api.Assertions.*;

import com.search.wiki.entity.Article;
import org.junit.jupiter.api.Test;

class ArticleTest {

  @Test
  void testEquals_SameInstance_ReturnsTrue() {
    Article article = new Article("Title", "https://example.com", "/images/article.jpg");
    assertEquals(article, article);
  }

  @Test
  void testEquals_Null_ReturnsFalse() {
    Article article = new Article("Title", "https://example.com", "/images/article.jpg");
    assertNotEquals(null, article);
  }

  @Test
  void testEquals_DifferentClass_ReturnsFalse() {
    Article article = new Article("Title", "https://example.com", "/images/article.jpg");
    assertNotEquals("SomeString", article);
  }

  @Test
  void testEquals_EqualObjects_ReturnsTrue() {
    Article article1 = new Article("Title", "https://example.com", "/images/article.jpg");
    Article article2 = new Article("Title", "https://example.com", "/images/article.jpg");
    assertEquals(article1, article2);
  }

  @Test
  void testEquals_DifferentObjects_ReturnsFalse() {
    Article article1 = new Article("Title", "https://example.com", "/images/article.jpg");
    Article article2 = new Article("DifferentTitle", "https://example.com", "/images/article.jpg");
    assertNotEquals(article1, article2);
  }

  @Test
  void testHashCode_EqualObjects_ReturnsSameHashCode() {
    Article article1 = new Article("Title", "https://example.com", "/images/article.jpg");
    Article article2 = new Article("Title", "https://example.com", "/images/article.jpg");
    assertEquals(article1.hashCode(), article2.hashCode());
  }

  @Test
  void testHashCode_DifferentObjects_ReturnsDifferentHashCode() {
    Article article1 = new Article("Title", "https://example.com", "/images/article.jpg");
    Article article2 = new Article("DifferentTitle", "https://example.com", "/images/different.jpg");
    assertNotEquals(article1.hashCode(), article2.hashCode());
  }

  @Test
  void testConstructorAndGetters() {
    String title = "Title";
    String url = "https://example.com";
    String imagePath = "/images/article.jpg";

    Article article = new Article(title, url, imagePath);

    assertEquals(title, article.getTitle());
    assertEquals(url, article.getUrl());
    assertEquals(imagePath, article.getImagePath());
  }
}
