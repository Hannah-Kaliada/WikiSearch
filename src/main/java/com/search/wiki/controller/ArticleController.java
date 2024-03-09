package com.search.wiki.controller;

import com.search.wiki.model.Article;
import com.search.wiki.model.Query;
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
    public ResponseEntity<String> findById(@PathVariable long id) {
        Article article = service.findById(id);
        if (article != null) {
            return ResponseEntity.ok(article.toString());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ARTICLE_NOT_FOUND_MESSAGE);
        }
    }

    @PutMapping("updateArticle")
    public ResponseEntity<String> updateArticle(@RequestBody Article article) {
        Article updatedArticle = service.updateArticle(article);
        if (updatedArticle != null) {
            return ResponseEntity.ok(updatedArticle.toString());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ARTICLE_NOT_FOUND_MESSAGE);
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
}
