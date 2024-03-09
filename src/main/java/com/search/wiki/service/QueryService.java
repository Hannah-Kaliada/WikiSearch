package com.search.wiki.service;

import com.search.wiki.model.Query;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
public class QueryService {
    public Query getSearchResult(String word) {
        return Query.builder().searchTerm("That's hard-coded JSON result: " + word).build();
    }
}
