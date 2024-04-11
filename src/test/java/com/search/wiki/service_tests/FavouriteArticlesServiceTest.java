package com.search.wiki.service_tests;

import com.search.wiki.cache.Cache;
import com.search.wiki.repository.ArticleRepository;
import com.search.wiki.repository.UserRepository;
import com.search.wiki.service.FavouriteArticlesService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.BeforeEach;


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


}
