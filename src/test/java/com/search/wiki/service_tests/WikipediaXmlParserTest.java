package com.search.wiki.service_tests;

import static org.junit.jupiter.api.Assertions.*;

import com.search.wiki.entity.Article;
import com.search.wiki.service.WikipediaXmlParser;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

/** The type Wikipedia xml parser test. */
class WikipediaXmlParserTest {

  private final WikipediaXmlParser xmlParser = new WikipediaXmlParser();

  /**
   * Parse xml valid xml returns list of articles.
   *
   * @throws ParserConfigurationException the parser configuration exception
   * @throws IOException the io exception
   * @throws SAXException the sax exception
   */
  @Test
  void parseXml_ValidXml_ReturnsListOfArticles()
      throws ParserConfigurationException, IOException, SAXException {

    String validXml =
        "<Items>"
            + "<Item><Text>Article 1</Text><Url>url1</Url><Image source=\"image1.jpg\"/></Item>"
            + "<Item><Text>Article 2</Text><Url>url2</Url><Image source=\"image2.jpg\"/></Item>"
            + "</Items>";

    List<Article> articles = xmlParser.parseXml(validXml);

    assertNotNull(articles);
    assertEquals(2, articles.size());

    Article article1 = articles.get(0);
    assertEquals("Article 1", article1.getTitle());
    assertEquals("url1", article1.getUrl());
    assertEquals("image1.jpg", article1.getImagePath());

    Article article2 = articles.get(1);
    assertEquals("Article 2", article2.getTitle());
    assertEquals("url2", article2.getUrl());
    assertEquals("image2.jpg", article2.getImagePath());
  }

  /**
   * Parse xml empty xml returns empty list of articles.
   *
   * @throws ParserConfigurationException the parser configuration exception
   * @throws IOException the io exception
   * @throws SAXException the sax exception
   */
  @Test
  void parseXml_EmptyXml_ReturnsEmptyListOfArticles()
      throws ParserConfigurationException, IOException, SAXException {

    String emptyXml = "";

    List<Article> articles = xmlParser.parseXml(emptyXml);

    assertNotNull(articles);
    assertEquals(0, articles.size());
  }
}
