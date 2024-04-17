package com.search.wiki.service_tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.search.wiki.cache.Cache;
import com.search.wiki.controller.dto.ArticleDto;
import com.search.wiki.controller.dto.FavouriteArticlesDto;
import com.search.wiki.entity.Article;
import com.search.wiki.entity.Country;
import com.search.wiki.entity.User;
import com.search.wiki.exceptions.customexceptions.NotFoundException;
import com.search.wiki.repository.ArticleRepository;
import com.search.wiki.repository.UserRepository;
import com.search.wiki.service.FavouriteArticlesService;
import jakarta.persistence.EntityManager;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** The type Favourite articles service test. */
@ExtendWith(MockitoExtension.class)
class FavouriteArticlesServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private ArticleRepository articleRepository;
  @Mock private EntityManager entityManager;

  @Mock private Cache cache;

  @InjectMocks private FavouriteArticlesService service;

  /** Gets user favorite articles user exists in cache returns favorite articles. */
  @Test
  void getUserFavoriteArticles_userExistsInCache_returnsFavoriteArticles() {
    Long userId = 1L;
    User user = createUserWithCountry("US");
    when(cache.get(anyString())).thenReturn(user);
    when(entityManager.find(User.class, userId)).thenReturn(user);

    FavouriteArticlesDto favouriteArticles = service.getUserFavoriteArticles(userId);

    assertNotNull(favouriteArticles);
    assertEquals(userId, favouriteArticles.getUserId());
  }

  /**
   * Gets user favorite articles user exists in database but not in cache returns favorite articles.
   */
  @Test
  void getUserFavoriteArticles_userExistsInDatabaseButNotInCache_returnsFavoriteArticles() {
    Long userId = 1L;
    User user = createUserWithCountry("US");
    when(cache.get(anyString())).thenReturn(null);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(entityManager.find(User.class, userId)).thenReturn(user);

    FavouriteArticlesDto favouriteArticles = service.getUserFavoriteArticles(userId);

    assertNotNull(favouriteArticles);
    assertEquals(userId, favouriteArticles.getUserId());
  }

  /** Gets user favorite articles user does not exist throws not found exception. */
  @Test
  void getUserFavoriteArticles_userDoesNotExist_throwsNotFoundException() {
    Long userId = 1L;
    when(cache.get(anyString())).thenReturn(null);
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> service.getUserFavoriteArticles(userId));
  }

  /** Gets articles saved by user article exists in cache returns users. */
  @Test
  void getArticlesSavedByUser_articleExistsInCache_returnsUsers() {
    Long articleId = 1L;
    Article article = new Article();
    article.setId(articleId);
    when(cache.get(anyString())).thenReturn(article);
    when(entityManager.find(Article.class, articleId)).thenReturn(article);

    Set<User> users = service.getArticlesSavedByUser(articleId);

    assertNotNull(users);
    assertTrue(users.stream().allMatch(user -> user.getFavoriteArticles().contains(article)));
  }

  /** Gets articles saved by user article exists in database but not in cache returns users. */
  @Test
  void getArticlesSavedByUser_articleExistsInDatabaseButNotInCache_returnsUsers() {
    Long articleId = 1L;
    Article article = new Article();
    article.setId(articleId);
    when(cache.get(anyString())).thenReturn(null);
    when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
    when(entityManager.find(Article.class, articleId)).thenReturn(article);

    Set<User> users = service.getArticlesSavedByUser(articleId);

    assertNotNull(users);
    assertTrue(users.stream().allMatch(user -> user.getFavoriteArticles().contains(article)));
  }

  /** Gets articles saved by user article does not exist throws not found exception. */
  @Test
  void getArticlesSavedByUser_articleDoesNotExist_throwsNotFoundException() {
    Long articleId = 1L;
    when(cache.get(anyString())).thenReturn(null);
    when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> service.getArticlesSavedByUser(articleId));
  }

  /** Edit user favorite article all entities exist in cache edits successfully. */
  @Test
  void editUserFavoriteArticle_allEntitiesExistInCache_editsSuccessfully() {
    Long userId = 1L;
    Long prevArticleId = 2L;
    Long newArticleId = 3L;

    User user = new User();
    Article prevArticle = new Article();
    Article newArticle = new Article();

    when(cache.get(anyString())).thenReturn(user).thenReturn(prevArticle).thenReturn(newArticle);
    when(entityManager.find(User.class, userId)).thenReturn(user);

    service.editUserFavoriteArticle(userId, prevArticleId, newArticleId);

    assertTrue(user.getFavoriteArticles().contains(newArticle));
  }

  /**
   * Edit user favorite article all entities exist in database but not in cache edits successfully.
   */
  @Test
  void editUserFavoriteArticle_allEntitiesExistInDatabaseButNotInCache_editsSuccessfully() {
    Long userId = 1L;
    Long prevArticleId = 2L;
    Long newArticleId = 3L;

    User user = createUserWithCountry("US");
    Article prevArticle = new Article();
    Article newArticle = new Article();

    when(cache.get(anyString())).thenReturn(null);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(articleRepository.findById(prevArticleId)).thenReturn(Optional.of(prevArticle));
    when(articleRepository.findById(newArticleId)).thenReturn(Optional.of(newArticle));
    when(entityManager.find(User.class, userId)).thenReturn(user);

    service.editUserFavoriteArticle(userId, prevArticleId, newArticleId);

    assertTrue(user.getFavoriteArticles().contains(newArticle));
  }

  /** Edit user favorite article user does not exist throws not found exception. */
  @Test
  void editUserFavoriteArticle_userDoesNotExist_throwsNotFoundException() {
    Long userId = 1L;
    Long prevArticleId = 2L;
    Long newArticleId = 3L;

    when(cache.get(anyString())).thenReturn(null);
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class,
        () -> service.editUserFavoriteArticle(userId, prevArticleId, newArticleId));
  }

  /** Edit user favorite article prev article does not exist throws not found exception. */
  @Test
  void editUserFavoriteArticle_prevArticleDoesNotExist_throwsNotFoundException() {
    Long userId = 1L;
    Long prevArticleId = 2L;
    Long newArticleId = 3L;

    User user = createUserWithCountry("US");
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    assertThrows(
        NotFoundException.class,
        () -> service.editUserFavoriteArticle(userId, prevArticleId, newArticleId));
  }

  /** Edit user favorite article new article does not exist throws not found exception. */
  @Test
  void editUserFavoriteArticle_newArticleDoesNotExist_throwsNotFoundException() {
    Long userId = 1L;
    Long prevArticleId = 2L;
    Long newArticleId = 3L;

    User user = createUserWithCountry("US");
    Article prevArticle = new Article();
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    assertThrows(
        NotFoundException.class,
        () -> service.editUserFavoriteArticle(userId, prevArticleId, newArticleId));

    verify(userRepository).findById(userId);
  }

  /** Remove article from user favorites user exists in cache removes successfully. */
  @Test
  void removeArticleFromUserFavorites_userExistsInCache_removesSuccessfully() {
    Long userId = 1L;
    Long articleId = 2L;

    User user = createUserWithCountry("US");
    Article articleToRemove = new Article();
    articleToRemove.setId(articleId);
    user.getFavoriteArticles().add(articleToRemove);
    when(cache.get(anyString())).thenReturn(user);
    when(entityManager.find(User.class, userId)).thenReturn(user);
    service.removeArticleFromUserFavorites(userId, articleId);
    assertFalse(user.getFavoriteArticles().contains(articleToRemove));
  }

  /**
   * Remove article from user favorites user exists in database but not in cache removes
   * successfully.
   */
  @Test
  void removeArticleFromUserFavorites_userExistsInDatabaseButNotInCache_removesSuccessfully() {
    Long userId = 1L;
    Long articleId = 2L;

    User user = createUserWithCountry("US");
    Article articleToRemove = new Article();
    articleToRemove.setId(articleId);
    user.getFavoriteArticles().add(articleToRemove);
    when(cache.get(anyString())).thenReturn(null);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(entityManager.find(User.class, userId)).thenReturn(user);

    service.removeArticleFromUserFavorites(userId, articleId);

    assertFalse(user.getFavoriteArticles().contains(articleToRemove));
  }

  /** Remove article from user favorites user does not exist throws not found exception. */
  @Test
  void removeArticleFromUserFavorites_userDoesNotExist_throwsNotFoundException() {
    Long userId = 1L;
    Long articleId = 2L;

    when(cache.get(anyString())).thenReturn(null);
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class, () -> service.removeArticleFromUserFavorites(userId, articleId));
  }

  /** Add article to user favorites user and article exist in cache adds successfully. */
  @Test
  void addArticleToUserFavorites_userAndArticleExistInCache_addsSuccessfully() {
    Long userId = 1L;
    Long articleId = 2L;

    User user = createUserWithCountry("US");
    Article article = new Article();
    article.setId(articleId);

    when(cache.get(anyString())).thenReturn(user).thenReturn(article);
    when(entityManager.find(User.class, userId)).thenReturn(user);

    service.addArticleToUserFavorites(userId, articleId);

    assertTrue(user.getFavoriteArticles().contains(article));
  }

  /**
   * Add article to user favorites user and article exist in database but not in cache adds
   * successfully.
   */
  @Test
  void addArticleToUserFavorites_userAndArticleExistInDatabaseButNotInCache_addsSuccessfully() {
    Long userId = 1L;
    Long articleId = 2L;

    User user = createUserWithCountry("US");
    Article article = new Article();
    article.setId(articleId);

    when(cache.get(anyString())).thenReturn(null);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
    when(entityManager.find(User.class, userId)).thenReturn(user);

    service.addArticleToUserFavorites(userId, articleId);

    assertTrue(user.getFavoriteArticles().contains(article));
  }

  /** Add article to user favorites user does not exist throws not found exception. */
  @Test
  void addArticleToUserFavorites_userDoesNotExist_throwsNotFoundException() {
    Long userId = 1L;
    Long articleId = 2L;

    when(cache.get(anyString())).thenReturn(null);
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class, () -> service.addArticleToUserFavorites(userId, articleId));
  }

  /** Add article to user favorites article does not exist throws not found exception. */
  @Test
  void addArticleToUserFavorites_articleDoesNotExist_throwsNotFoundException() {
    Long userId = 1L;
    Long articleId = 2L;

    User user = createUserWithCountry("US");

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    assertThrows(
        NotFoundException.class, () -> service.addArticleToUserFavorites(userId, articleId));
  }

  /** Convert to article dto set all fields correct converts successfully. */
  @Test
  void convertToArticleDtoSet_allFieldsCorrect_convertsSuccessfully() {

    Set<Article> articles = new HashSet<>();
    Article article1 = new Article();
    article1.setId(1L);
    article1.setTitle("Title 1");
    article1.setUrl("URL 1");
    article1.setImagePath("Path 1");
    articles.add(article1);

    Article article2 = new Article();
    article2.setId(2L);
    article2.setTitle("Title 2");
    article2.setUrl("URL 2");
    article2.setImagePath("Path 2");
    articles.add(article2);

    Set<ArticleDto> articleDtos = service.convertToArticleDtoSet(articles);

    assertNotNull(articleDtos);
    assertEquals(2, articleDtos.size());
    for (ArticleDto articleDto : articleDtos) {
      assertTrue(articleDto.getId() == 1L || articleDto.getId() == 2L);
      assertTrue(articleDto.getTitle().startsWith("Title"));
      assertTrue(articleDto.getUrl().startsWith("URL"));
      assertTrue(articleDto.getImagePath().startsWith("Path"));
    }
  }

  /** Convert to article dto set empty set converts successfully. */
  @Test
  void convertToArticleDtoSet_emptySet_convertsSuccessfully() {

    Set<Article> articles = new HashSet<>();

    Set<ArticleDto> articleDtos = service.convertToArticleDtoSet(articles);

    assertNotNull(articleDtos);
    assertTrue(articleDtos.isEmpty());
  }

  /**
   * Create user with country user.
   *
   * @param countryCode the country code
   * @return the user
   */
  static User createUserWithCountry(String countryCode) {
    User user = new User();
    Country country = new Country();
    country.setName(countryCode);
    user.setId(1L);
    user.setCountry(country);
    return user;
  }
}
