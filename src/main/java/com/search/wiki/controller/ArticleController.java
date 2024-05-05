package com.search.wiki.controller;

import com.search.wiki.entity.Article;
import com.search.wiki.entity.Query;
import com.search.wiki.exceptions.customexceptions.NotFoundException;
import com.search.wiki.service.ArticleService;
import com.search.wiki.service.WikipediaApiService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** The type Article controller. */
@RestController
@RequestMapping("api/v1/articles")
@AllArgsConstructor
public class ArticleController {
  private static final String ARTICLE_NOT_FOUND_MESSAGE = "Article not found";

  @NonNull private final ArticleService service;
  @NonNull private final WikipediaApiService wikipediaApiService;

  /**
   * Find all articles response entity.
   *
   * @return the response entity
   */
  @GetMapping()
  @CrossOrigin
  public ResponseEntity<List<Article>> findAllArticles() {
    List<Article> articles = service.findAllArticles();
    return ResponseEntity.ok(articles);
  }

  /**
   * Save article response entity.
   *
   * @param article the article
   * @return the response entity
   */
  @PostMapping("/saveArticle")
  @CrossOrigin
  public ResponseEntity<String> saveArticle(@RequestBody Article article) {
    service.saveArticle(article);
    String title = article.getTitle();

    if (title == null || title.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid article title.");
    }
    Query query = new Query(title);
    wikipediaApiService.search(query);
    return ResponseEntity.status(HttpStatus.CREATED).body("Article saved successfully!");
  }

  /**
   * Find by id response entity.
   *
   * @param id the id
   * @return the response entity
   */
  @GetMapping("/{id}")
  @CrossOrigin
  public ResponseEntity<Article> findById(@PathVariable long id) {

    Article article = service.findById(id);
    if (article != null) {
      return ResponseEntity.ok(article);
    } else {
      throw new NotFoundException(ARTICLE_NOT_FOUND_MESSAGE);
    }
  }

  /**
   * Update article response entity.
   *
   * @param article the article
   * @param id the id
   * @return the response entity
   */
  @PutMapping("updateArticle/{id}")
  @CrossOrigin
  public ResponseEntity<Article> updateArticle(
      @Valid @RequestBody Article article, @PathVariable long id) {

    Article updatedArticle = service.updateArticle(article, id);
    if (updatedArticle != null) {
      return ResponseEntity.ok(updatedArticle);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  /**
   * Delete article response entity.
   *
   * @param id the id
   * @return the response entity
   */
  @DeleteMapping("deleteArticle/{id}")
  @CrossOrigin
  public ResponseEntity<String> deleteArticle(@PathVariable long id) {
    boolean deleted = service.deleteArticle(id);
    if (deleted) {
      return ResponseEntity.ok("Article deleted successfully!");
    } else {
      throw new NotFoundException(ARTICLE_NOT_FOUND_MESSAGE);
    }
  }

  /**
   * Gets top 5 articles by user count.
   *
   * @return the top 5 articles by user count
   */
  @GetMapping("/top5ByUserCount")
  @CrossOrigin
  public ResponseEntity<List<Article>> getTop5ArticlesByUserCount() {
    List<Article> top5Articles = service.findTop5ArticlesByUserCount();
    return ResponseEntity.ok(top5Articles);
  }

  /**
   * Search articles list.
   *
   * @param keyword the keyword
   * @return the list
   */
  @GetMapping("/search")
  @CrossOrigin
  public List<Article> searchArticles(@RequestParam("keyword") String keyword) {
    return service.searchArticlesByKeyword(keyword);
  }

  /**
   * Bulk search articles response entity.
   *
   * @param queries the queries
   * @return the response entity
   */
  @PostMapping("/bulkSearchArticles")
  @CrossOrigin
  public ResponseEntity<String> bulkSearchArticles(@RequestBody List<Query> queries) {
    if (queries == null || queries.isEmpty()) {
      return ResponseEntity.badRequest().body("List of queries is empty.");
    }

    List<CompletableFuture<Void>> searchFutures =
        queries.stream()
            .map(query -> CompletableFuture.runAsync(() -> wikipediaApiService.search(query)))
            .toList();

    CompletableFuture<Void> allSearches =
        CompletableFuture.allOf(searchFutures.toArray(new CompletableFuture[0]));

    try {
      allSearches.get();
      return ResponseEntity.ok("Bulk search completed successfully!");
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Bulk search was interrupted: " + e.getMessage());
    } catch (ExecutionException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Bulk search encountered an error: " + e.getCause().getMessage());
    }
  }
}
