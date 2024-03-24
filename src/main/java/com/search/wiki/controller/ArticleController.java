package com.search.wiki.controller;

import com.search.wiki.entity.Article;
import com.search.wiki.entity.Query;
import com.search.wiki.service.ArticleService;
import com.search.wiki.service.WikipediaApiService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/articles")
@AllArgsConstructor
public class ArticleController {
    private static final String ARTICLE_NOT_FOUND_MESSAGE = "Article not found";

    @NonNull
    private final ArticleService service;
    @NonNull
    private final WikipediaApiService wikipediaApiService;

    @GetMapping()
    public ResponseEntity<List<Article>> findAllArticles() {
        List<Article> articles = service.findAllArticles();
        return ResponseEntity.ok(articles);
    }

    @PostMapping("saveArticle")
    public ResponseEntity<String> saveArticle(@RequestBody Article article) {
        service.saveArticle(article);
        Query query = new Query(article.getTitle());
        wikipediaApiService.search(query);
        return ResponseEntity.status(HttpStatus.CREATED).body("Article saved successfully!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> findById(@PathVariable long id) {
        Article article = service.findById(id);
        if (article != null) {
            return ResponseEntity.ok(article);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("updateArticle/{id}")
    public ResponseEntity<Article> updateArticle(@RequestBody Article article, @PathVariable Long id) {
        Article updatedArticle = service.updateArticle(article, id);
        if (updatedArticle != null) {
            return ResponseEntity.ok(updatedArticle);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @DeleteMapping("deleteArticle/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable long id) {
        boolean deleted = service.deleteArticle(id);
        if (deleted) {
            return ResponseEntity.ok("Article deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ARTICLE_NOT_FOUND_MESSAGE);
        }
    }

    @GetMapping("/top5ByUserCount")
    public ResponseEntity<List<Article>> getTop5ArticlesByUserCount() {
        List<Article> top5Articles = service.findTop5ArticlesByUserCount();
        return ResponseEntity.ok(top5Articles);
    }
}
