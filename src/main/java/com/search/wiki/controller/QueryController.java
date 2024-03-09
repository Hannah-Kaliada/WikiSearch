package com.search.wiki.controller;

import com.search.wiki.model.Article;
import com.search.wiki.model.Query;
import com.search.wiki.service.WikipediaApiService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Data
@RequestMapping("/api/v1/search")
public class QueryController {

    private final WikipediaApiService wikipediaApiService;

    @Autowired
    public QueryController(WikipediaApiService wikipediaApiService) {
        this.wikipediaApiService = wikipediaApiService;
    }

    @GetMapping("/getSearchResult")
    public String getSearchResult(@RequestParam String searchTerm) {
        Query query = new Query(searchTerm);
        List<Article> articles = wikipediaApiService.search(query);

        // Iterate through the list of articles and print information
        for (Article article : articles) {
            System.out.println("Title: " + article.getTitle());
            System.out.println("URL: " + article.getUrl());
            System.out.println("Snippet: " + article.getSnippet());
            System.out.println("Image Path: " + article.getImagePath());
            System.out.println();
        }

        // You can return a response or simply return an empty string for this example
        return "";
    }
}
