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
@RequestMapping("api/v1")
@AllArgsConstructor
public class ArticleController {
    @NonNull
    private final ArticleService service;
    @NonNull
    private final WikipediaApiService wikipediaApiService;
    @GetMapping("getAllArticles")
    public ResponseEntity<List<Article>> findAllArticles() {
        List<Article> articles = service.findAllArticles();
        return ResponseEntity.ok(articles);
    }

    @PostMapping("saveArticle")
    public ResponseEntity<String> saveArticle(@RequestBody Article article) {
        // Save the provided article
        service.saveArticle(article);

        // Trigger Wikipedia API search and save
        Query query = new Query(article.getTitle()); // Assuming the article title is used as the search term
        wikipediaApiService.search(query);
        return ResponseEntity.status(HttpStatus.CREATED).body("Article saved successfully!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        Article article = service.findById(id);
        if (article != null) {
            return ResponseEntity.ok(article);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article not found");
        }
    }

    @PutMapping("updateArticle")
    public ResponseEntity<?> updateArticle(@RequestBody Article article) {
        Article updatedArticle = service.updateArticle(article);
        if (updatedArticle != null) {
            return ResponseEntity.ok(updatedArticle);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article not found");
        }
    }

    @DeleteMapping("deleteArticle/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable long id) {
        boolean deleted = service.deleteArticle(id);
        if (deleted) {
            return ResponseEntity.ok("Article deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article not found");
        }
    }
}
