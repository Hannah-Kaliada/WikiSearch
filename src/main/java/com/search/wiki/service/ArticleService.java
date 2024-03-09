package com.search.wiki.service;

import com.search.wiki.model.Article;
import com.search.wiki.repository.ArticleDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ArticleService {

     private final ArticleDAO repository;

     public Article saveArticle(Article article) {
          return repository.saveArticle(article);
     }

     public Article findById(long id) {
          return repository.findById(id);
     }

     public Article updateArticle(Article article) {
          return repository.updateArticle(article);
     }

     public boolean deleteArticle(long id) {
          return repository.deleteArticle(id);
     }

     public List<Article> findAllArticles() {
          return repository.findAllArticles();
     }
}
