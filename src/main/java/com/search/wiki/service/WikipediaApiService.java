package com.search.wiki.service;

import com.search.wiki.entity.Article;
import com.search.wiki.entity.Query;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/** The type Wikipedia api service. */
@Service
public class WikipediaApiService {

  private final WikipediaXmlParser wikipediaXmlParser;
  private final ArticleService articleService;

  /**
   * Instantiates a new Wikipedia api service.
   *
   * @param wikipediaXmlParser the wikipedia xml parser
   * @param articleService the article service
   */
  @Autowired
  public WikipediaApiService(WikipediaXmlParser wikipediaXmlParser, ArticleService articleService) {
    this.wikipediaXmlParser = wikipediaXmlParser;
    this.articleService = articleService;
  }

  /**
   * Search list.
   *
   * @param query the query
   * @return the list
   */
  public List<Article> search(Query query) {
    String apiUrl = "https://ru.wikipedia.org/w/api.php";
    String action = "opensearch";
    String format = "xml";
    String inprop = "url";
    int limit = 100;

    String fullUrl =
        String.format(
            "%s?action=%s&search=%s&format=%s&inprop=%s&limit=%d",
            apiUrl, action, query.getSearchTerm(), format, inprop, limit);

    RestTemplate restTemplate = new RestTemplate();
    String xmlResponse = restTemplate.getForObject(fullUrl, String.class);

    List<Article> articles = wikipediaXmlParser.parseXml(xmlResponse);

    articles.parallelStream().forEach(articleService::saveArticle);

    return articles;
  }
}
