package com.search.wiki.service_tests;

import com.search.wiki.cache.Cache;
import com.search.wiki.entity.Article;
import com.search.wiki.entity.User;
import com.search.wiki.repository.ArticleRepository;
import com.search.wiki.repository.UserRepository;
import com.search.wiki.service.FavouriteArticlesService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FavouriteArticlesServiceTest {

		@Mock
		private UserRepository userRepository;

		@Mock
		private ArticleRepository articleRepository;

		@Mock
		private EntityManager entityManager;

		@Mock
		private Cache cache;

		@InjectMocks
		private FavouriteArticlesService favouriteArticlesService;

		// Предварительные настройки перед каждым тестом
		@BeforeEach
		public void setUp() {
				// Можно настроить поведение mock-объектов здесь, если это необходимо
		}


		// Напишите аналогичные тесты для других методов FavouriteArticlesService
}
