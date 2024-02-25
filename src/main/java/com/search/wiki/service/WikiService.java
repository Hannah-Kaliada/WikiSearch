package com.search.wiki.service;

import com.search.wiki.model.SearchResult;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class WikiService {
    public SearchResult getSearchResult(@RequestParam String word)
    {
        return SearchResult.builder().query("That's hard coded JSON result: "+word).build();
    }
}
