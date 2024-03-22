package com.search.wiki.service;

import com.search.wiki.entity.Article;
import com.search.wiki.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

     // Метод для получения топ 5 статей по количеству добавивших пользователей
     public List<Article> findTop5ArticlesByUserCount() {
          // Получаем результат запроса
          List<Object[]> result = repository.findTop5ArticlesByUserCount();

          // Создаем список статей
          List<Article> top5Articles = new ArrayList<>();

          // Проверяем, есть ли результат
          if (result != null && !result.isEmpty()) {
               // Преобразуем массивы объектов в объекты статей и добавляем их в список
               for (Object[] row : result) {
                    Article article = (Article) row[0];
                    top5Articles.add(article);
               }
          }

          // Возвращаем только топ 5 статей
          return top5Articles.size() > 5 ? top5Articles.subList(0, 5) : top5Articles;
     }
}
