package com.search.wiki.controller;

import com.search.wiki.model.SearchResult;
import com.search.wiki.service.WikiService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/search")
@AllArgsConstructor
public class SearchController {
    private final WikiService service;

    @GetMapping("/getSearchResult")
    public SearchResult getSearchResult(@RequestParam String word)
    {
        return service.getSearchResult(word);
    }
}
