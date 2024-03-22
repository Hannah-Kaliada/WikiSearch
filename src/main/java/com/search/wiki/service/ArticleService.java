package com.search.wiki.service;

import com.search.wiki.entity.Article;
import com.search.wiki.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
     private final ArticleRepository repository;

     public ArticleService(ArticleRepository repository) {
          this.repository = repository;
     }

     public Article saveArticle(Article article) {
          return repository.save(article);
     }

     public Article findById(long id) {
          return repository.findById(id).orElse(null);
     }

     public Article updateArticle(Article article) {
          return repository.save(article);
     }

     public boolean deleteArticle(long id) {
          try {
               repository.deleteById(id);
               return true;
          } catch (Exception e) {
               return false;
          }
     }

     public List<Article> findAllArticles() {
          return repository.findAll();
     }
}
