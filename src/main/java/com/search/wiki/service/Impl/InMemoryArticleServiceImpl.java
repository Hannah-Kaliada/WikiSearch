package com.search.wiki.service.Impl;

import com.search.wiki.model.Article;
import com.search.wiki.repository.ArticleDAO;
import com.search.wiki.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InMemoryArticleServiceImpl implements ArticleService {

    private final ArticleDAO repository;

    public InMemoryArticleServiceImpl(ArticleDAO repository) {
        this.repository = repository;
    }

    @Override
    public Article saveArticle(Article article) {
        return repository.saveArticle(article);
    }

    @Override
    public Article findById(long id) {
        return repository.findById(id);
    }

    @Override
    public Article updateArticle(Article article) {
        return repository.updateArticle(article);
    }

    @Override
    public boolean deleteArticle(long id) {
        return repository.deleteArticle(id);
    }

    @Override
    public List<Article> findAllArticles() {
        return repository.findAllArticles();
    }
}
