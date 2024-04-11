package com.search.wiki.controller_tests;

import static org.mockito.Mockito.*;

import com.search.wiki.controller.QueryController;
import com.search.wiki.entity.Query;
import com.search.wiki.service.WikipediaApiService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

 class QueryControllerTest {

  @Test
  void testGetSearchResult() throws Exception {
    // Mock для WikipediaApiService
    WikipediaApiService wikipediaApiService = mock(WikipediaApiService.class);

    // Создание контроллера и передача мока сервиса
    QueryController queryController = new QueryController(wikipediaApiService);

    // Настройка MockMvc
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(queryController).build();

    // Выполнение запроса и проверка результата
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/search/getSearchResult")
                .param("searchTerm", "Java")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("Java"));

    // Проверка вызова метода search() у wikipediaApiService с объектом Query
    verify(wikipediaApiService, times(1)).search(any(Query.class));
  }

  @Test
  void testGetSearchResultWithEmptySearchTerm() throws Exception {
    WikipediaApiService wikipediaApiService = mock(WikipediaApiService.class);
    QueryController queryController = new QueryController(wikipediaApiService);
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(queryController).build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/search/getSearchResult")
                .param("searchTerm", "")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

    // Проверяем, что метод search() не вызывается при пустом поисковом запросе
    verify(wikipediaApiService, never()).search(any(Query.class));
  }

  @Test
  void testGetSearchResultWithSpecialCharacters() throws Exception {
    WikipediaApiService wikipediaApiService = mock(WikipediaApiService.class);
    QueryController queryController = new QueryController(wikipediaApiService);
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(queryController).build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/search/getSearchResult")
                .param("searchTerm", "!@#$%^&*()")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("!@#$%^&*()"));

    // Проверяем, что метод search() вызывается с объектом Query при специальных символах в запросе
    verify(wikipediaApiService, times(1)).search(any(Query.class));
  }
  @Test
  void testGetSearchResultWithNotFound() throws Exception {
    WikipediaApiService wikipediaApiService = mock(WikipediaApiService.class);
    QueryController queryController = new QueryController(wikipediaApiService);
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(queryController).build();

    String searchTerm = "NonexistentTerm";
    mockMvc
            .perform(
                    MockMvcRequestBuilders.get("/api/v1/search/getSearchResult")
                            .param("searchTerm", searchTerm)
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(searchTerm));

    // Проверяем, что метод search() вызывается с объектом Query и возвращает пустой результат
    verify(wikipediaApiService, times(1)).search(any(Query.class));
    // Здесь можно добавить дополнительную проверку на обработку пустого результата
  }
  @Test
  void testGetSearchResultWithLongSearchTerm() throws Exception {
    WikipediaApiService wikipediaApiService = mock(WikipediaApiService.class);
    QueryController queryController = new QueryController(wikipediaApiService);
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(queryController).build();

    // Создаем длинный поисковый запрос
    String longSearchTerm = "a".repeat(100); // Например, запрос из 100 символов

    mockMvc
            .perform(
                    MockMvcRequestBuilders.get("/api/v1/search/getSearchResult")
                            .param("searchTerm", longSearchTerm)
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(longSearchTerm));

    // Проверяем, что метод search() вызывается с объектом Query для длинного запроса
    verify(wikipediaApiService, times(1)).search(any(Query.class));
  }
  @Test
  void testGetSearchResultWithNoSearchTerm() throws Exception {
    WikipediaApiService wikipediaApiService = mock(WikipediaApiService.class);
    QueryController queryController = new QueryController(wikipediaApiService);
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(queryController).build();

    mockMvc
            .perform(
                    MockMvcRequestBuilders.get("/api/v1/search/getSearchResult")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

    // Проверяем, что метод search() не вызывается при отсутствии параметра searchTerm
    verify(wikipediaApiService, never()).search(any(Query.class));
  }
}
