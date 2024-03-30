package com.search.wiki.controller;

import com.search.wiki.entity.Query;
import com.search.wiki.service.WikipediaApiService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** The type Query controller. */
@RestController
@Data
@RequestMapping("/api/v1/search")
public class QueryController {

  private final WikipediaApiService wikipediaApiService;

  /**
   * Instantiates a new Query controller.
   *
   * @param wikipediaApiService the wikipedia api service
   */
  @Autowired
  public QueryController(WikipediaApiService wikipediaApiService) {
    this.wikipediaApiService = wikipediaApiService;
  }

  /**
   * Gets search result.
   *
   * @param searchTerm the search term
   * @return the search result
   */
  @GetMapping("/getSearchResult")
  public String getSearchResult(@RequestParam String searchTerm) {
    Query query = new Query(searchTerm);
    wikipediaApiService.search(query);
    return "";
  }
}
