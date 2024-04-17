package com.search.wiki.service;

import com.search.wiki.cache.Cache;
import com.search.wiki.entity.Article;
import com.search.wiki.exceptions.ExceptionConstants;
import com.search.wiki.exceptions.customexceptions.DatabaseAccessException;
import com.search.wiki.exceptions.customexceptions.NotFoundException;
import com.search.wiki.repository.ArticleRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/** The type Article service. */
@Service
public class ArticleService {
  private final ArticleRepository repository;
  private final Cache cache;
  private static final String ARTICLE_CACHE_PREFIX = "Article_";


  /**
   * Instantiates a new Article service.
   *
   * @param repository the repository
   * @param cache the cache
   */
  public ArticleService(ArticleRepository repository, Cache cache) {
    this.repository = repository;
    this.cache = cache;
  }

  /**
   * Save article article.
   *
   * @param article the article
   * @return the article
   * @throws IllegalArgumentException if article is null
   */
  public Article saveArticle(Article article) {
    if (article == null) {
      throw new IllegalArgumentException("Article cannot be null");
    }

    Article savedArticle = repository.save(article);

    cache.put(getCacheKey(savedArticle.getId()), savedArticle);
    return savedArticle;
  }

  /**
   * Find by id article.
   *
   * @param id the id
   * @return the article
   */
  public Article findById(long id) {
    if (id < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
    }
    String cacheKey = getCacheKey(id);
    return getCachedOrFromRepository(cacheKey, id);
  }

  /**
   * Update article article.
   *
   * @param article the article
   * @param id the id
   * @return the article
   */
  public Article updateArticle(Article article, Long id) {
    if (id < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
    }
    Article existingArticle = findById(id);

    if (existingArticle != null) {
      existingArticle.setTitle(article.getTitle());
      existingArticle.setUrl(article.getUrl());
      existingArticle.setImagePath(article.getImagePath());

      Article updatedArticle = repository.save(existingArticle);

      String cacheKey = getCacheKey(updatedArticle.getId());
      cache.put(cacheKey, updatedArticle);

      return updatedArticle;
    } else {
      throw new NotFoundException(ExceptionConstants.ARTICLE_NOT_FOUND + id);
    }
  }

  /**
   * Delete article boolean.
   *
   * @param id the id
   * @return the boolean
   */
  public boolean deleteArticle(long id) {
    if (id < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
    }
    Optional<Article> articleOptional = repository.findById(id);

    if (articleOptional.isPresent()) {
      repository.deleteById(id);
      boolean articleDeleted = !repository.existsById(id);
      if (articleDeleted) {
        cache.remove(getCacheKey(id));
        return true;
      } else {
        throw new DatabaseAccessException("Error deleting article with id: " + id);
      }
    } else {
      throw new NotFoundException(ExceptionConstants.ARTICLE_NOT_FOUND + id);
    }
  }

  /**
   * Find all articles list.
   *
   * @return the list
   */
  public List<Article> findAllArticles() {
    List<Article> articles = repository.findAll();
    if (articles.isEmpty()) {
      throw new NotFoundException("No articles found");
    }
    articles.forEach(article -> cache.put(getCacheKey(article.getId()), article));
    return articles;
  }

  /**
   * Find top 5 articles by user count list.
   *
   * @return the list
   */
  public List<Article> findTop5ArticlesByUserCount() {
    Pageable pageable = PageRequest.of(0, 5);
    return repository.findTop5ArticlesByUserCount(pageable);
  }

  private String getCacheKey(long id) {
    return ARTICLE_CACHE_PREFIX + id;
  }

  private Article getCachedOrFromRepository(String cacheKey, long id) {
    if (id < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
    }
    if (cache.containsKey(cacheKey)) {
      return (Article) cache.get(cacheKey);
    } else {
      Article article = repository.findById(id).orElse(null);
      if (article != null) {
        cache.put(cacheKey, article);
      } else {
        throw new NotFoundException(ExceptionConstants.ARTICLE_NOT_FOUND + id);
      }
      return article;
    }
  }

  /**
   * Search articles by keyword list.
   *
   * @param keyword the keyword
   * @return the list
   */
  public List<Article> searchArticlesByKeyword(String keyword) {
    return repository.findByTitleContaining(keyword);
  }
}
