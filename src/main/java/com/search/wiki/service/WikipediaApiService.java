package com.search.wiki.service;

import com.search.wiki.model.Article;
import com.search.wiki.model.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class WikipediaApiService {

    private final WikipediaXmlParser wikipediaXmlParser;
    private final ArticleService articleService;

    @Autowired
    public WikipediaApiService(WikipediaXmlParser wikipediaXmlParser, ArticleService articleService) {
        this.wikipediaXmlParser = wikipediaXmlParser;
        this.articleService = articleService;
    }

    public List<Article> search(Query query) {
        String apiUrl = "https://ru.wikipedia.org/w/api.php";
        String action = "opensearch";
        String format = "xml";
        String inprop = "url";

        String fullUrl = String.format(
                "%s?action=%s&search=%s&format=%s&inprop=%s",
                apiUrl, action, query.getSearchTerm(), format, inprop
        );

        RestTemplate restTemplate = new RestTemplate();
        String xmlResponse = restTemplate.getForObject(fullUrl, String.class);

        List<Article> articles = wikipediaXmlParser.parseXml(xmlResponse);

        // Save the fetched articles using the ArticleService
        for (Article article : articles) {
            articleService.saveArticle(article);
        }

        return articles; // Return the list of articles if needed
    }
}
