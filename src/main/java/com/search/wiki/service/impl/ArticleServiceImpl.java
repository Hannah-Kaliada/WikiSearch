package com.search.wiki.service.impl;

import com.search.wiki.model.Article;
import com.search.wiki.repository.ArticleRepository;
import com.search.wiki.service.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Primary
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository repository;

    @Override
    public Article saveArticle(Article article) {
        return repository.save(article);
    }

    @Override
    public Article findById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Article updateArticle(Article article) {
        return repository.save(article);
    }

    @Override
    public boolean deleteArticle(long id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Article> findAllArticles() {
        return repository.findAll();
    }
}
