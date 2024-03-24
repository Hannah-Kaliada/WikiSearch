package com.search.wiki.service;

import com.search.wiki.cache.Cache;
import com.search.wiki.entity.Article;
import com.search.wiki.repository.ArticleRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository repository;
    private final Cache cache;
    private static final String ARTICLE_CACHE_PREFIX = "Article_";

    public ArticleService(ArticleRepository repository, Cache cache) {
        this.repository = repository;
        this.cache = cache;
    }

    public Article saveArticle(Article article) {
        Article savedArticle = repository.save(article);
        cache.put(getCacheKey(savedArticle.getId()), savedArticle);
        return savedArticle;
    }

    public Article findById(long id) {
        String cacheKey = getCacheKey(id);
        return getCachedOrFromRepository(cacheKey, id);
    }

    public Article updateArticle(Article article, Long id) {
        Article existingArticle = findById(id);

        if (existingArticle != null) {
            existingArticle.setTitle(article.getTitle());
            existingArticle.setUrl(article.getUrl());
            existingArticle.setImagePath(article.getImagePath());

            Article updatedArticle = repository.save(existingArticle);

            if (updatedArticle != null) {
                String cacheKey = getCacheKey(updatedArticle.getId());
                cache.put(cacheKey, updatedArticle);
            }

            return updatedArticle;
        } else {
            return null;
        }
    }

    public boolean deleteArticle(long id) {
        try {
            repository.deleteById(id);
            cache.remove(getCacheKey(id));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Article> findAllArticles() {
        List<Article> articles = repository.findAll();
        for (Article article : articles) {
            cache.put(getCacheKey(article.getId()), article);
        }
        return articles;
    }

    public List<Article> findTop5ArticlesByUserCount() {
        Pageable pageable = PageRequest.of(0, 5);
        return repository.findTop5ArticlesByUserCount(pageable);
    }

    private String getCacheKey(long id) {
        return ARTICLE_CACHE_PREFIX + id;
    }

    private Article getCachedOrFromRepository(String cacheKey, long id) {
        if (cache.containsKey(cacheKey)) {
            return (Article) cache.get(cacheKey);
        } else {
            Article article = repository.findById(id).orElse(null);
            if (article != null) {
                cache.put(cacheKey, article);
            }
            return article;
        }
    }

    public List<Article> searchArticlesByKeyword(String keyword) {
        return repository.findByTitleContaining(keyword);
    }
}
