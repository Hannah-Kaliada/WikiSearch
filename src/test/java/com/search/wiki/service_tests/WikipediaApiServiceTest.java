package com.search.wiki.service_tests;

import static org.mockito.Mockito.*;

import com.search.wiki.entity.Article;
import com.search.wiki.entity.Query;
import com.search.wiki.repository.ArticleRepository;
import com.search.wiki.service.WikipediaApiService;
import com.search.wiki.service.WikipediaXmlParser;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class WikipediaApiServiceTest {

  @Mock private WikipediaXmlParser wikipediaXmlParser;

  @Mock private ArticleRepository articleRepository;

  @InjectMocks private WikipediaApiService wikipediaApiService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSearchWithValidQuery() throws Exception {

    Query query = new Query("searchTerm");

    String xmlResponse = "<xml>...</xml>";
    when(wikipediaXmlParser.parseXml(xmlResponse)).thenReturn(createMockArticles());

    List<Article> result = wikipediaApiService.search(query);
  }

  @Test
  void testSearchWithInvalidQuery() throws Exception {

    Query query = new Query(null);

    List<Article> result = wikipediaApiService.search(query);
  }

  private List<Article> createMockArticles() {

    List<Article> articles = new ArrayList<>();
    Article article1 = new Article("Title1", "URL1", "Image1");
    Article article2 = new Article("Title2", "URL2", "Image2");
    articles.add(article1);
    articles.add(article2);
    return articles;
  }
}
