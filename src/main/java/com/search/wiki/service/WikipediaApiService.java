package com.search.wiki.service;

import com.search.wiki.entity.Article;
import com.search.wiki.entity.Query;
import com.search.wiki.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class WikipediaApiService {

    private final WikipediaXmlParser wikipediaXmlParser;
    private final ArticleService articleService;
    private final ArticleRepository articleRepository;

    @Autowired
    public WikipediaApiService(WikipediaXmlParser wikipediaXmlParser, ArticleService articleService, ArticleRepository articleRepository) {
        this.wikipediaXmlParser = wikipediaXmlParser;
        this.articleService = articleService;
        this.articleRepository = articleRepository;
    }

    public List<Article> search(Query query) {
        String apiUrl = "https://ru.wikipedia.org/w/api.php";
        String action = "opensearch";
        String format = "xml";
        String inprop = "url";
        int limit = 100;

        String fullUrl = String.format(
                "%s?action=%s&search=%s&format=%s&inprop=%s&limit=%d",
                apiUrl, action, query.getSearchTerm(), format, inprop, limit
        );

        RestTemplate restTemplate = new RestTemplate();
        String xmlResponse = restTemplate.getForObject(fullUrl, String.class);

        List<Article> articles = wikipediaXmlParser.parseXml(xmlResponse);

        for (Article article : articles) {
            // Проверяем, существует ли статья в базе данных по заголовку
            if (!articleRepository.existsByTitle(article.getTitle())) {
                articleRepository.save(article);
            }
        }


        return articles;
    }

}
