package com.search.wiki.controller_tests;

import static org.mockito.Mockito.*;

import com.search.wiki.controller.WikipediaApiController;
import com.search.wiki.entity.Article;
import com.search.wiki.entity.Query;
import com.search.wiki.service.WikipediaApiService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class WikipediaApiControllerTest {

  @Test
  void testSearch() throws Exception {
    // Mock для WikipediaApiService
    WikipediaApiService wikipediaApiService = mock(WikipediaApiService.class);

    // Создание контроллера и передача мока сервиса
    WikipediaApiController wikipediaApiController = new WikipediaApiController(wikipediaApiService);

    // Настройка MockMvc
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(wikipediaApiController).build();

    // Создание объекта Query
    Query query = new Query("Java");

    // Мок-ответ от WikipediaApiService
    List<Article> articles = new ArrayList<>();
    Article article1 = new Article();
    article1.setTitle("Java Programming");
    article1.setUrl("https://en.wikipedia.org/wiki/Java_Programming");
    Article article2 = new Article();
    article2.setTitle("Java Virtual Machine");
    article2.setUrl("https://en.wikipedia.org/wiki/Java_Virtual_Machine");
    articles.add(article1);
    articles.add(article2);
    when(wikipediaApiService.search(query)).thenReturn(articles);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/v1/search")
                .content("{\"searchTerm\": \"Java\"}")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.length()").value(2)) // Проверка на количество статей
        .andExpect(
            MockMvcResultMatchers.jsonPath("$[0].title")
                .value("Java Programming")) // Проверка на наличие первой статьи
        .andExpect(
            MockMvcResultMatchers.jsonPath("$[1].title")
                .value("Java Virtual Machine")); // Проверка на наличие второй статьи

    // Проверка вызова метода search() у wikipediaApiService с объектом Query
    verify(wikipediaApiService, times(1)).search(query);
  }
}
