package com.search.wiki.service.Implemention;

import com.search.wiki.model.SearchResult;
import com.search.wiki.service.WikiService;
import org.springframework.stereotype.Service;

@Service
public class SearchQueryImplemention implements WikiService
{
    @Override
    public SearchResult getSearchResult(String word) {
        return SearchResult.builder().query("That's hard coded JSON result: "+word).build();
    }
}
