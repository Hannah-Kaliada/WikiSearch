package com.search.wiki;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class WikiApplicationTests {

    @Autowired
    private DataSource dataSource;

    @Test
    void databaseIsRunning() throws SQLException {
        assertNotNull(dataSource);

        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection);
        }
    }
}
