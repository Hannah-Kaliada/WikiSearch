package com.search.wiki.dto_tests;

import static org.junit.jupiter.api.Assertions.*;

import com.search.wiki.controller.dto.ArticleDto;
import com.search.wiki.controller.dto.CountryDto;
import com.search.wiki.controller.dto.FavouriteArticlesDto;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FavouriteArticlesDtoTest {

  private FavouriteArticlesDto dto1;
  private FavouriteArticlesDto dto2;

  @BeforeEach
  void setUp() {
    dto1 = new FavouriteArticlesDto();
    dto1.setUserId(1L);
    dto1.setUsername("user1");
    dto1.setEmail("user1@example.com");
    dto1.setCountry(new CountryDto());
    dto1.setPassword("password");

    Set<ArticleDto> articles1 = new HashSet<>();
    ArticleDto article1 = new ArticleDto();
    article1.setId(1L);
    article1.setTitle("Article 1");
    articles1.add(article1);

    ArticleDto article2 = new ArticleDto();
    article2.setId(2L);
    article2.setTitle("Article 2");
    articles1.add(article2);

    dto1.setFavouriteArticles(articles1);

    dto2 = new FavouriteArticlesDto();
    dto2.setUserId(1L);
    dto2.setUsername("user1");
    dto2.setEmail("user1@example.com");
    dto2.setCountry(new CountryDto());
    dto2.setPassword("password");

    Set<ArticleDto> articles2 = new HashSet<>();
    ArticleDto article3 = new ArticleDto();
    article3.setId(1L);
    article3.setTitle("Article 1");
    articles2.add(article3);

    ArticleDto article4 = new ArticleDto();
    article4.setId(2L);
    article4.setTitle("Article 2");
    articles2.add(article4);

    dto2.setFavouriteArticles(articles2);
  }

  @Test
  void testEquals_SameInstance_ReturnsTrue() {
    assertEquals(dto1, dto1);
  }

  @Test
  void testEquals_Null_ReturnsFalse() {
    assertNotEquals(null, dto1);
  }

  @Test
  void testEquals_DifferentClass_ReturnsFalse() {
    assertNotEquals("SomeString", dto1);
  }

  @Test
  void testEquals_EqualObjects_ReturnsTrue() {
    assertEquals(dto1, dto2);
  }

  @Test
  void testEquals_DifferentUserId_ReturnsFalse() {
    dto2.setUserId(2L);
    assertNotEquals(dto1, dto2);
  }

  @Test
  void testEquals_DifferentUsername_ReturnsFalse() {
    dto2.setUsername("user2");
    assertNotEquals(dto1, dto2);
  }

  @Test
  void testHashCode_EqualObjects_ReturnsSameHashCode() {
    assertEquals(dto1.hashCode(), dto2.hashCode());
  }

  @Test
  void testHashCode_DifferentObjects_ReturnsDifferentHashCode() {
    dto2.setUsername("user2");
    assertNotEquals(dto1.hashCode(), dto2.hashCode());
  }
  @Test
  void testGettersAndSetters() {
    assertEquals(1L, dto1.getUserId());
    assertEquals("user1", dto1.getUsername());
    assertEquals("user1@example.com", dto1.getEmail());
    assertEquals("password", dto1.getPassword());

    CountryDto country1 = dto1.getCountry();
    assertNotNull(country1);
    Set<ArticleDto> articles1 = dto1.getFavouriteArticles();
    assertNotNull(articles1);
    assertEquals(2, articles1.size());
  }
}
