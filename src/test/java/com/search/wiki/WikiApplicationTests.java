package com.search.wiki;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/** The type Wiki application tests. */
@SpringBootTest
class WikiApplicationTests {

  @Autowired private DataSource dataSource;

  /**
   * Database is running.
   *
   * @throws SQLException the sql exception
   */
  @Test
  void databaseIsRunning() throws SQLException {
    assertNotNull(dataSource);

    try (Connection connection = dataSource.getConnection()) {
      assertNotNull(connection);
    }
  }
}
