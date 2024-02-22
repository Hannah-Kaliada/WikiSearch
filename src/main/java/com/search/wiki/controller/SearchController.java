package com.search.wiki.controller;

import com.search.wiki.model.SearchResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/search")
public class SearchController {
    @GetMapping("/getSearchResult")
    public SearchResult getSearchResult(@RequestParam String word)
    {
        return SearchResult.builder().query("That's hard coded JSON result: "+word).build();
    }
}
