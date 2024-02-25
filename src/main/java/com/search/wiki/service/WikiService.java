package com.search.wiki.service;

import com.search.wiki.model.SearchResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public interface WikiService {
     SearchResult getSearchResult(@RequestParam String word);
}
