package com.search.wiki.controller;

import com.search.wiki.entity.Article;
import com.search.wiki.entity.Query;
import com.search.wiki.service.WikipediaApiService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The type Wikipedia api controller. */
@RestController
@RequestMapping("/api/v1")
public class WikipediaApiController {

  private final WikipediaApiService wikipediaApiService;

  /**
   * Instantiates a new Wikipedia api controller.
   *
   * @param wikipediaApiService the wikipedia api service
   */
  @Autowired
  public WikipediaApiController(WikipediaApiService wikipediaApiService) {
    this.wikipediaApiService = wikipediaApiService;
  }

  /**
   * Search response entity.
   *
   * @param query the query
   * @return the response entity
   */
  @PostMapping("/search")
  public ResponseEntity<List<Article>> search(@RequestBody Query query) {
    List<Article> articles = wikipediaApiService.search(query);
    return ResponseEntity.ok(articles);
  }
}
