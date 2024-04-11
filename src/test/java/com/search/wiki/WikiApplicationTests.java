package com.search.wiki;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WikiApplicationTests {

  @Autowired private ApplicationContext applicationContext;

  @LocalServerPort private int port;

  private final RestTemplate restTemplate = new RestTemplate();

  @Test
  void contextLoads() {
    assertNotNull(applicationContext);
  }

  @Test
  void applicationStartsAndRespondsOK() {
    ResponseEntity<String> response =
        restTemplate.getForEntity("http://localhost:" + port + "/", String.class);
    assertTrue(response.getStatusCode().is2xxSuccessful());
  }
  @Test
  void mainMethodRunsSpringApplication() {
    try (MockedStatic<SpringApplication> mockedStatic = mockStatic(SpringApplication.class)) {
      WikiApplication.main(new String[]{});
      mockedStatic.verify(() -> SpringApplication.run(WikiApplication.class, new String[]{}));
    }
  }
}
