package com.search.wiki.controller;

import com.search.wiki.entity.Article;
import com.search.wiki.entity.Query;
import com.search.wiki.exceptions.customexceptions.NotFoundException;
import com.search.wiki.service.ArticleService;
import com.search.wiki.service.WikipediaApiService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<String> saveArticle(@RequestBody Article article) {
    // Simulate saving the article (replace this with your actual save logic)
    service.saveArticle(article);

    // Assume you want to search Wikipedia using the article's title
    String title = article.getTitle(); // Get the title from the saved article

    if (title == null || title.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid article title.");
    }

    // Construct the query object and perform the search
    Query query = new Query(title);
    wikipediaApiService.search(query);

    // Respond with a success message
    return ResponseEntity.status(HttpStatus.CREATED).body("Article saved successfully!");
  }

  /**
   * Find by id response entity.
   *
   * @param id the id
   * @return the response entity
   */
  @GetMapping("/{id}")
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
  public List<Article> searchArticles(@RequestParam("keyword") String keyword) {
    return service.searchArticlesByKeyword(keyword);
  }
}
