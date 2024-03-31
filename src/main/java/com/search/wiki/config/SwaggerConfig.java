package com.search.wiki.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/** Configuration class for Swagger/OpenAPI documentation. */
@Configuration
@OpenAPIDefinition(
    info =
        @Info(
            title = "Wiki Search API",
            version = "1.0",
            description = "API for searching articles in Wikipedia.",
            contact =
                @Contact(
                    name = "Hanna Kaliada",
                    email = "Koleda.ann@gmail.com",
                    url = "https://t.me/jewishmommy"),
            license =
                @License(
                    name = "Apache 2.0",
                    url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
            termsOfService = "http://localhost:8080/termsOfUse.html"),
    servers = {
      @Server(url = "http://localhost:8080", description = "Local server"),
    })
public class SwaggerConfig {}
