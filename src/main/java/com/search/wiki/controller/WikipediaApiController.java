package com.search.wiki.controller;

import com.search.wiki.entity.Article;
import com.search.wiki.entity.Query;
import com.search.wiki.service.WikipediaApiService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
   * @param searchTerm the query
   * @return the response entity
   */
  @PostMapping("/search")
  @CrossOrigin
  public ResponseEntity<List<Article>> search(@RequestParam("searchTerm") String searchTerm) {
    Query query = new Query();
    query.setSearchTerm(searchTerm);
    List<Article> articles = wikipediaApiService.search(query);
    return ResponseEntity.ok(articles);
  }
}