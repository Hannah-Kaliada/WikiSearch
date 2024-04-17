package com.search.wiki.service;

import com.search.wiki.cache.Cache;
import com.search.wiki.controller.dto.ArticleDto;
import com.search.wiki.controller.dto.FavouriteArticlesDto;
import com.search.wiki.controller.dto.UserDto;
import com.search.wiki.entity.Article;
import com.search.wiki.entity.User;
import com.search.wiki.exceptions.ExceptionConstants;
import com.search.wiki.exceptions.customexceptions.NotFoundException;
import com.search.wiki.repository.ArticleRepository;
import com.search.wiki.repository.UserRepository;
import com.search.wiki.service.utils.ConvertToDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** The type Favourite articles service. */
@Service
@AllArgsConstructor
@Transactional
public class FavouriteArticlesService {

  private final UserRepository userRepository;
  private final ArticleRepository articleRepository;
  private final Cache cache;

  @PersistenceContext private EntityManager entityManager;

  /**
   * Add article to user favorites.
   *
   * @param userId the user id
   * @param articleId the article id
   */
  public void addArticleToUserFavorites(Long userId, Long articleId) {
    if (userId < 1 || articleId < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
    }
    User user = getUserFromCache(userId);
    Article article = getArticleFromCache(articleId);

    if (user == null) {
      user = userRepository.findById(userId).orElse(null);
      if (user != null) {
        cache.put(getUserCacheKey(userId), user);
      } else {
        throw new NotFoundException(ExceptionConstants.USER_NOT_FOUND + userId);
      }
    }
    if (article == null) {
      article = articleRepository.findById(articleId).orElse(null);
      if (article != null) {
        cache.put(getArticleCacheKey(articleId), article);
      } else {
        throw new NotFoundException(ExceptionConstants.ARTICLE_NOT_FOUND + articleId);
      }
    }
    entityManager.detach(user);
    user = entityManager.find(User.class, userId);
    user.getFavoriteArticles().add(article);
    userRepository.save(user);
    cache.put(getUserCacheKey(userId), user);
  }

  /**
   * Remove article from user favorites.
   *
   * @param userId the user id
   * @param articleId the article id
   */
  public void removeArticleFromUserFavorites(long userId, long articleId) {
    if (userId < 1 || articleId < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
    }
    String userCacheKey = getUserCacheKey(userId);
    User user = (User) cache.get(userCacheKey);

    if (user == null) {
      user = userRepository.findById(userId).orElse(null);
      if (user != null) {
        cache.put(userCacheKey, user);
      } else {
        throw new NotFoundException(ExceptionConstants.USER_NOT_FOUND + userId);
      }
    }

    entityManager.detach(user);
    user = entityManager.find(User.class, userId);
    user.getFavoriteArticles().removeIf(article -> article.getId() == articleId);
    userRepository.save(user);
    cache.remove(userCacheKey);
  }

  /**
   * Edit user favorite article.
   *
   * @param userId the user id
   * @param prevArticleId the prev article id
   * @param newArticleId the new article id
   */
  public void editUserFavoriteArticle(Long userId, Long prevArticleId, Long newArticleId) {
    if (userId < 1 || prevArticleId < 1 || newArticleId < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
    }
    String userCacheKey = getUserCacheKey(userId);

    User user = (User) cache.get(userCacheKey);
    Article prevArticle = (Article) cache.get(getArticleCacheKey(prevArticleId));
    Article newArticle = (Article) cache.get(getArticleCacheKey(newArticleId));

    if (user == null) {
      user = userRepository.findById(userId).orElse(null);
      if (user != null) {
        cache.put(userCacheKey, user);
      } else throw new NotFoundException(ExceptionConstants.USER_NOT_FOUND + userId);
    }

    if (prevArticle == null) {
      prevArticle = articleRepository.findById(prevArticleId).orElse(null);
      if (prevArticle != null) {
        cache.put(getArticleCacheKey(prevArticleId), prevArticle);
      } else throw new NotFoundException(ExceptionConstants.ARTICLE_NOT_FOUND + prevArticleId);
    }

    if (newArticle == null) {
      newArticle = articleRepository.findById(newArticleId).orElse(null);
      if (newArticle != null) {
        cache.put(getArticleCacheKey(newArticleId), newArticle);
      } else throw new NotFoundException(ExceptionConstants.ARTICLE_NOT_FOUND + newArticleId);
    }

    entityManager.detach(user);
    user = entityManager.find(User.class, userId);

    user.getFavoriteArticles().remove(prevArticle);
    prevArticle.getUsers().remove(user);
    user.getFavoriteArticles().add(newArticle);
    newArticle.getUsers().add(user);

    articleRepository.save(prevArticle);
    articleRepository.save(newArticle);

    userRepository.save(user);

    cache.remove(userCacheKey);
  }

  /**
   * Gets user favorite articles.
   *
   * @param userId the user id
   * @return the user favorite articles
   */
  public FavouriteArticlesDto getUserFavoriteArticles(Long userId) {
    if (userId < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
    }

    final String userCacheKey = getUserCacheKey(userId);
    User user = (User) cache.get(userCacheKey);

    if (user == null) {
      user = userRepository.findById(userId).orElse(null);
      if (user != null) {
        cache.put(userCacheKey, user);
      } else {
        throw new NotFoundException(ExceptionConstants.USER_NOT_FOUND + userId);
      }
    }

    entityManager.detach(user);
    user = entityManager.find(User.class, userId);

    final FavouriteArticlesDto favouriteArticlesDto = new FavouriteArticlesDto();
    UserDto userDto = ConvertToDto.convertUserToDto(user);
    favouriteArticlesDto.setUserId(userId);
    Set<ArticleDto> articleDtoSet = convertToArticleDtoSet(user.getFavoriteArticles());
    favouriteArticlesDto.setEmail(user.getEmail());
    favouriteArticlesDto.setUsername(user.getUsername());
    favouriteArticlesDto.setCountry(userDto.getCountry());
    favouriteArticlesDto.setPassword(user.getPassword());
    favouriteArticlesDto.setFavouriteArticles(articleDtoSet);

    return favouriteArticlesDto;
  }


  /**
   * Gets articles saved by user.
   *
   * @param articleId the article id
   * @return the articles saved by user
   */
  public Set<User> getArticlesSavedByUser(Long articleId) {
    if (articleId < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
    }

    final String articleCacheKey = getArticleCacheKey(articleId);
    Article article = (Article) cache.get(articleCacheKey);
    final Set<User> users;

    if (article == null) {
      article = articleRepository.findById(articleId).orElse(null);
      if (article != null) {
        cache.put(articleCacheKey, article);
      } else {
        throw new NotFoundException(ExceptionConstants.ARTICLE_NOT_FOUND + articleId);
      }
    }

    entityManager.detach(article);
    article = entityManager.find(Article.class, article.getId());
    users = new HashSet<>(article.getUsers());

    return users;
  }

  public Set<ArticleDto> convertToArticleDtoSet(Set<Article> articles) {
    return articles.stream()
            .map(article -> {
              ArticleDto articleDto = new ArticleDto();
              articleDto.setId(article.getId());
              articleDto.setTitle(article.getTitle());
              articleDto.setUrl(article.getUrl());
              articleDto.setImagePath(article.getImagePath());
              return articleDto;
            })
            .collect(Collectors.toSet());
  }


  private User getUserFromCache(Long userId) {
    return (User) cache.get(getUserCacheKey(userId));
  }

  private Article getArticleFromCache(Long articleId) {
    return (Article) cache.get(getArticleCacheKey(articleId));
  }

  private String getUserCacheKey(Long userId) {
    return "User_" + userId;
  }

  private String getArticleCacheKey(Long articleId) {
    return "Article_" + articleId;
  }
}
