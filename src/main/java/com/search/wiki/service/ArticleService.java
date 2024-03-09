package com.search.wiki.service;

import com.search.wiki.entity.Article;

import java.util.List;

public interface ArticleService {
     Article saveArticle(Article article);
     Article findById(long id);
     Article updateArticle(Article article);
     boolean deleteArticle(long id);
     List<Article> findAllArticles();
}
