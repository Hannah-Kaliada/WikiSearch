package com.search.wiki.entity_tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.search.wiki.entity.Article;
import com.search.wiki.entity.Country;
import com.search.wiki.entity.User;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void testNoArgsConstructor() {
    User user = new User();
    assertNotNull(user);
  }

  @Test
  void testAllArgsConstructor() {
    Country mockCountry = mock(Country.class);
    User user = new User("username", "email", "password", mockCountry);
    assertNotNull(user);
    assertEquals("username", user.getUsername());
    assertEquals("email", user.getEmail());
    assertEquals("password", user.getPassword());
    assertEquals(mockCountry, user.getCountry());
  }

  @Test
  void testAllArgsConstructor_NullCountry() {
    // Создаем пользователя с null в качестве country
    User user = new User("username", "email", "password", null);

    // Проверяем, что country равен null
    assertNull(user.getCountry());
  }

  @Test
  void testGettersAndSetters() {
    User user = new User();

    user.setId(1L);
    assertEquals(1L, user.getId());

    user.setUsername("username");
    assertEquals("username", user.getUsername());

    user.setEmail("email");
    assertEquals("email", user.getEmail());

    user.setPassword("password");
    assertEquals("password", user.getPassword());

    Country country = new Country();
    user.setCountry(country);
    assertEquals(country, user.getCountry());
  }

  @Test
  void testEquals() {
    Country country = new Country();
    User user1 = new User("username", "email", "password", country);
    User user2 = new User("username", "email", "password", country);
    assertEquals(user1, user2);
  }

  @Test
  void testNotEquals() {
    Country country1 = new Country();
    Country country2 = new Country();
    User user1 = new User("username1", "email", "password", country1);
    User user2 = new User("username", "email", "password", country2);
    assertNotEquals(user1, user2);
  }

  @Test
  void testHashCode() {
    Country country = new Country();
    User user1 = new User("username", "email", "password", country);
    User user2 = new User("username", "email", "password", country);
    assertEquals(user1.hashCode(), user2.hashCode());
  }

  @Test
  void testHashCode_NotEqualObjects() {
    Country country1 = new Country();
    Country country2 = new Country();
    User user1 = new User("username", "emails", "password", country1);
    User user2 = new User("username", "email", "password", country2);
    assertNotEquals(user1.hashCode(), user2.hashCode());
  }

  @Test
  void testAddFavoriteArticle() {
    User user = new User();
    Article article = new Article("Title", "url", "imagePath");
    user.getFavoriteArticles().add(article);
    assertTrue(user.getFavoriteArticles().contains(article));
  }

  @Test
  void testCountryAssociation() {
    Country country = new Country();
    User user = new User();
    user.setCountry(country);
    assertEquals(country, user.getCountry());
  }
}
