package com.search.wiki.service;

import com.search.wiki.entity.Article;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Service
public class WikipediaXmlParser {

  private static final Logger logger = LoggerFactory.getLogger(WikipediaXmlParser.class);

  public List<Article> parseXml(String xml) {
    List<Article> articles = new ArrayList<>();
    try {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = builder.parse(new InputSource(new StringReader(xml)));

      NodeList itemNodes = doc.getElementsByTagName("Item");

      for (int i = 0; i < itemNodes.getLength(); i++) {
        Node itemNode = itemNodes.item(i);
        if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
          Element itemElement = (Element) itemNode;

          String title = getElementTextByTagName(itemElement, "Text");
          String url = getElementTextByTagName(itemElement, "Url");
          String imagePath = getElementAttributeByTagName(itemElement, "Image", "source");

          Article article = new Article();
          article.setTitle(title);
          article.setUrl(url);
          article.setImagePath(imagePath);

          articles.add(article);
        }
      }
    } catch (Exception e) {
      logger.error("Error parsing XML: {}", e.getMessage(), e);
    }
    return articles;
  }

  private String getElementTextByTagName(Element element, String tagName) {
    NodeList nodeList = element.getElementsByTagName(tagName);
    if (nodeList.getLength() > 0) {
      return nodeList.item(0).getTextContent();
    }
    return null;
  }

  private String getElementAttributeByTagName(
      Element element, String tagName, String attributeName) {
    NodeList nodeList = element.getElementsByTagName(tagName);
    if (nodeList.getLength() > 0) {
      Element elementWithTag = (Element) nodeList.item(0);
      return elementWithTag.getAttribute(attributeName);
    }
    return null;
  }
}