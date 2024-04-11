package com.search.wiki.controller_tests;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.search.wiki.controller.FavouriteArticlesController;
import com.search.wiki.controller.dto.FavouriteArticlesDto;
import com.search.wiki.entity.User;
import com.search.wiki.service.FavouriteArticlesService;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class FavouriteArticlesControllerTest {

		@Test
		void testAddArticleToUserFavorites() throws Exception {
				FavouriteArticlesService favouriteArticlesService = mock(FavouriteArticlesService.class);
				FavouriteArticlesController controller = new FavouriteArticlesController(favouriteArticlesService);
				MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

				mockMvc.perform(post("/api/v1/favorite-articles/1/add/100")
												.contentType(MediaType.APPLICATION_JSON))
								.andExpect(status().isOk());

				verify(favouriteArticlesService, times(1)).addArticleToUserFavorites(1L, 100L);
		}

		@Test
		void testRemoveArticleFromUserFavorites() throws Exception {
				FavouriteArticlesService favouriteArticlesService = mock(FavouriteArticlesService.class);
				FavouriteArticlesController controller = new FavouriteArticlesController(favouriteArticlesService);
				MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

				mockMvc.perform(delete("/api/v1/favorite-articles/1/remove/200")
												.contentType(MediaType.APPLICATION_JSON))
								.andExpect(status().isOk());

				verify(favouriteArticlesService, times(1)).removeArticleFromUserFavorites(1L, 200L);
		}

		@Test
		void testEditUserFavoriteArticle() throws Exception {
				FavouriteArticlesService favouriteArticlesService = mock(FavouriteArticlesService.class);
				FavouriteArticlesController controller = new FavouriteArticlesController(favouriteArticlesService);
				MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

				mockMvc.perform(put("/api/v1/favorite-articles/1/edit/300/400")
												.contentType(MediaType.APPLICATION_JSON))
								.andExpect(status().isOk());

				verify(favouriteArticlesService, times(1)).editUserFavoriteArticle(1L, 300L, 400L);
		}

		@Test
		void testGetArticlesSavedByUser() throws Exception {
				FavouriteArticlesService favouriteArticlesService = mock(FavouriteArticlesService.class);
				FavouriteArticlesController controller = new FavouriteArticlesController(favouriteArticlesService);
				MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

				Set<User> users = new HashSet<>();
				User john = new User();
				john.setId(1L);
				john.setUsername("John");
				users.add(john);

				when(favouriteArticlesService.getArticlesSavedByUser(500L)).thenReturn(users);

				mockMvc.perform(get("/api/v1/favorite-articles/500/get-saved-users")
												.contentType(MediaType.APPLICATION_JSON))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
								.andExpect(jsonPath("$[0].id").value(1))
								.andExpect(jsonPath("$[0].username").value("John"));
		}

		@Test
		void testGetUserFavoriteArticles() throws Exception {
				FavouriteArticlesService favouriteArticlesService = mock(FavouriteArticlesService.class);
				FavouriteArticlesController controller = new FavouriteArticlesController(favouriteArticlesService);
				MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

				FavouriteArticlesDto dto = new FavouriteArticlesDto();
				when(favouriteArticlesService.getUserFavoriteArticles(1L)).thenReturn(dto);

				mockMvc.perform(get("/api/v1/favorite-articles/1/get-favorite-articles")
												.contentType(MediaType.APPLICATION_JSON))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
		}
}
