package com.search.wiki.service;

import com.search.wiki.entity.Article;
import com.search.wiki.entity.Query;
import com.search.wiki.repository.ArticleRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;

/** The type Wikipedia api service. */
@Service
public class WikipediaApiService {

  private final WikipediaXmlParser wikipediaXmlParser;
  private final ArticleRepository articleRepository;

  @Autowired
  public WikipediaApiService(
      WikipediaXmlParser wikipediaXmlParser,
      ArticleRepository articleRepository) {
    this.wikipediaXmlParser = wikipediaXmlParser;
    this.articleRepository = articleRepository;
  }

  public List<Article> search(Query query) {
    if (query == null) {
      throw new IllegalArgumentException("Query cannot be null");
    }

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

    List<Article> articles = new ArrayList<>();

    try {
      articles = wikipediaXmlParser.parseXml(xmlResponse);
    } catch (ParserConfigurationException | SAXException | IOException e) {
      return articles; // Return empty list or handle accordingly
    }

    for (Article article : articles) {
      if (!articleRepository.existsByTitle(article.getTitle())) {
        articleRepository.save(article);
      }
    }

    return articles;
  }
}