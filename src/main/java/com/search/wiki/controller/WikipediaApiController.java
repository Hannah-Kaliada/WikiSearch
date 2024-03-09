package com.search.wiki.controller;

import com.search.wiki.model.Article;
import com.search.wiki.model.Query;
import com.search.wiki.service.WikipediaApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class WikipediaApiController {

    private final WikipediaApiService wikipediaApiService;

    @Autowired
    public WikipediaApiController(WikipediaApiService wikipediaApiService) {
        this.wikipediaApiService = wikipediaApiService;
    }

    @PostMapping("/search")
    public ResponseEntity<List<Article>> search(@RequestBody Query query) {
        List<Article> articles = wikipediaApiService.search(query);
        return ResponseEntity.ok(articles);
    }
}
