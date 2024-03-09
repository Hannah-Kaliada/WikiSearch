package com.search.wiki.repository;

import com.search.wiki.model.Article;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ArticleDAO {
    private final List<Article> ARTICLES = new ArrayList<>();

    public List<Article> findAllArticles() {
        return ARTICLES;
    }

    public Article saveArticle(Article article) {
        ARTICLES.add(article);
        return article;
    }

    public Article findById(long id) {
        return ARTICLES.stream().filter(article -> article.getId() == id).findFirst().orElse(null);
    }

    public Article updateArticle(Article article) {
        Optional<Article> existingArticle = ARTICLES.stream().filter(a -> a.getId() == article.getId()).findFirst();
        existingArticle.ifPresent(a -> {
            a.setTitle(article.getTitle());
            a.setUrl(article.getUrl());
            a.setSnippet(article.getSnippet());
            a.setImagePath(article.getImagePath());
        });
        return article;
    }
    public boolean deleteArticle(long id) {
        return ARTICLES.removeIf(article -> article.getId() == id);
    }
}
