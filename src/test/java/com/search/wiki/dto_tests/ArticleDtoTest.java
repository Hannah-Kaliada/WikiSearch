package com.search.wiki.dto_tests;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.search.wiki.controller.dto.ArticleDto;
import org.junit.jupiter.api.Test;

public class ArticleDtoTest {

  @Test
  public void testGetterAndSetter() {
    // Создаем объект ArticleDto
    ArticleDto articleDto = new ArticleDto();

    // Устанавливаем значения
    Long expectedId = 1L;
    String expectedTitle = "Test Title";
    String expectedUrl = "http://example.com/article";
    String expectedImagePath = "/images/article.jpg";

    articleDto.setId(expectedId);
    articleDto.setTitle(expectedTitle);
    articleDto.setUrl(expectedUrl);
    articleDto.setImagePath(expectedImagePath);

    // Проверяем, что значения установлены правильно с помощью геттеров
    assertEquals(expectedId, articleDto.getId());
    assertEquals(expectedTitle, articleDto.getTitle());
    assertEquals(expectedUrl, articleDto.getUrl());
    assertEquals(expectedImagePath, articleDto.getImagePath());
  }
  @Test
  public void testEqualsAndHashCode() {
    // Создаем два объекта ArticleDto с одинаковыми значениями полей
    ArticleDto articleDto1 = new ArticleDto();
    articleDto1.setId(1L);
    articleDto1.setTitle("Test Title");

    ArticleDto articleDto2 = new ArticleDto();
    articleDto2.setId(1L);
    articleDto2.setTitle("Test Title");

    // Проверяем, что методы equals() и hashCode() работают корректно
    assertEquals(articleDto1, articleDto2);
    assertEquals(articleDto1.hashCode(), articleDto2.hashCode());
  }
  @Test
  public void testNullValues() {
    ArticleDto articleDto = new ArticleDto();

    // Установка всех полей в null
    articleDto.setId(null);
    articleDto.setTitle(null);
    articleDto.setUrl(null);
    articleDto.setImagePath(null);

    // Проверка геттеров, что они возвращают null
    assertNull(articleDto.getId());
    assertNull(articleDto.getTitle());
    assertNull(articleDto.getUrl());
    assertNull(articleDto.getImagePath());
  }
  @Test
  public void testUpdateFields() {
    ArticleDto articleDto = new ArticleDto();

    // Установка начальных значений
    articleDto.setId(1L);
    articleDto.setTitle("Initial Title");

    // Обновление значений
    articleDto.setId(2L);
    articleDto.setTitle("Updated Title");

    // Проверка, что поля были успешно обновлены
    assertEquals(2L, articleDto.getId());
    assertEquals("Updated Title", articleDto.getTitle());
  }
  @Test
  public void testCreateInstanceAndCompareObjects() {
    ArticleDto articleDto1 = new ArticleDto();
    articleDto1.setId(1L);
    articleDto1.setTitle("Test Title");

    ArticleDto articleDto2 = new ArticleDto();
    articleDto2.setId(2L);
    articleDto2.setTitle("Another Title");

    // Проверка, что объекты созданы и не равны друг другу
    assertNotNull(articleDto1);
    assertNotNull(articleDto2);
    assertNotEquals(articleDto1, articleDto2);
  }
  @Test
  void testArticleDtoSerialization() throws JsonProcessingException {
    // Создание объекта ArticleDto
    ArticleDto articleDto = new ArticleDto();
    articleDto.setId(1L);
    articleDto.setTitle("Sample Article");
    articleDto.setUrl("https://example.com/article");
    articleDto.setImagePath("/images/sample.jpg");

    // Инициализация ObjectMapper для сериализации в JSON
    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(articleDto);

    // Проверка, что JSON содержит ожидаемые поля и значения
    assertEquals("{\"id\":1,\"title\":\"Sample Article\",\"url\":\"https://example.com/article\",\"imagePath\":\"/images/sample.jpg\"}", json);
  }

  @Test
  void testArticleDtoDeserialization() throws JsonProcessingException {
    // JSON-строка, которая будет десериализована в объект ArticleDto
    String json = "{\"id\":1,\"title\":\"Sample Article\",\"url\":\"https://example.com/article\",\"imagePath\":\"/images/sample.jpg\"}";

    // Инициализация ObjectMapper для десериализации из JSON
    ObjectMapper objectMapper = new ObjectMapper();
    ArticleDto articleDto = objectMapper.readValue(json, ArticleDto.class);

    // Проверка, что объект ArticleDto содержит ожидаемые значения после десериализации
    assertEquals(1L, articleDto.getId());
    assertEquals("Sample Article", articleDto.getTitle());
    assertEquals("https://example.com/article", articleDto.getUrl());
    assertEquals("/images/sample.jpg", articleDto.getImagePath());
  }
}
