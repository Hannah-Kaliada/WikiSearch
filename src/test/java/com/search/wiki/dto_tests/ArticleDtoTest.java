package com.search.wiki.dto_tests;

import static org.junit.jupiter.api.Assertions.*;

import com.search.wiki.controller.dto.ArticleDto;
import org.junit.jupiter.api.Test;

class ArticleDtoTest {

  @Test
  void testEquals_SameInstance_ReturnsTrue() {
    ArticleDto article = new ArticleDto();
    assertEquals(article, article);
  }

  @Test
  void testEquals_NullObject_ReturnsFalse() {
    ArticleDto article = new ArticleDto();
    assertNotEquals(null, article);
  }

  @Test
  void testEquals_DifferentClass_ReturnsFalse() {
    ArticleDto article = new ArticleDto();
    assertNotEquals("SomeString", article);
  }

  @Test
  void testEquals_EqualObjects_ReturnsTrue() {
    ArticleDto article1 = new ArticleDto();
    article1.setId(1L);
    article1.setTitle("Title");
    article1.setUrl("http://example.com");
    article1.setImagePath("/path/to/image");

    ArticleDto article2 = new ArticleDto();
    article2.setId(1L);
    article2.setTitle("Title");
    article2.setUrl("http://example.com");
    article2.setImagePath("/path/to/image");

    assertEquals(article1, article2);
  }

  @Test
  void testEquals_DifferentObjects_ReturnsFalse() {
    ArticleDto article1 = new ArticleDto();
    article1.setId(1L);
    article1.setTitle("Title");
    article1.setUrl("http://example.com");
    article1.setImagePath("/path/to/image");

    ArticleDto article2 = new ArticleDto();
    article2.setId(2L);
    article2.setTitle("Title");
    article2.setUrl("http://example.com");
    article2.setImagePath("/path/to/image");

    assertNotEquals(article1, article2);
  }

  @Test
  void testHashCode_EqualObjects_ReturnsSameHashCode() {
    ArticleDto article1 = new ArticleDto();
    article1.setId(1L);
    article1.setTitle("Title");
    article1.setUrl("http://example.com");
    article1.setImagePath("/path/to/image");

    ArticleDto article2 = new ArticleDto();
    article2.setId(1L);
    article2.setTitle("Title");
    article2.setUrl("http://example.com");
    article2.setImagePath("/path/to/image");

    assertEquals(article1.hashCode(), article2.hashCode());
  }

  @Test
  void testHashCode_DifferentObjects_ReturnsDifferentHashCode() {
    ArticleDto article1 = new ArticleDto();
    article1.setId(1L);
    article1.setTitle("Title");
    article1.setUrl("http://example.com");
    article1.setImagePath("/path/to/image");

    ArticleDto article2 = new ArticleDto();
    article2.setId(2L); // Different id
    article2.setTitle("Title");
    article2.setUrl("http://example.com");
    article2.setImagePath("/path/to/image");

    assertNotEquals(article1.hashCode(), article2.hashCode());
  }
  @Test
  void testSettersAndGetters() {
    ArticleDto article = new ArticleDto();

    Long expectedId = 1L;
    String expectedTitle = "Test Title";
    String expectedUrl = "http://example.com/article";
    String expectedImagePath = "/images/article.jpg";

    article.setId(expectedId);
    article.setTitle(expectedTitle);
    article.setUrl(expectedUrl);
    article.setImagePath(expectedImagePath);

    assertEquals(expectedId, article.getId());
    assertEquals(expectedTitle, article.getTitle());
    assertEquals(expectedUrl, article.getUrl());
    assertEquals(expectedImagePath, article.getImagePath());
  }

  @Test
  void testDefaultValues() {

    ArticleDto article = new ArticleDto();

    assertNull(article.getId());
    assertNull(article.getTitle());
    assertNull(article.getUrl());
    assertNull(article.getImagePath());
  }
}
