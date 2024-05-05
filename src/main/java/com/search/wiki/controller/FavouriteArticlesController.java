package com.search.wiki.controller;

import com.search.wiki.controller.dto.FavouriteArticlesDto;
import com.search.wiki.entity.User;
import com.search.wiki.service.FavouriteArticlesService;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** The type Favourite articles controller. */
@RestController
@RequestMapping("/api/v1/favorite-articles")
@AllArgsConstructor
public class FavouriteArticlesController {

  private final FavouriteArticlesService favouriteArticlesService;

  /**
   * Add article to user favorites response entity.
   *
   * @param userId the user id
   * @param articleId the article id
   * @return the response entity
   */
  @PostMapping("/{userId}/add/{articleId}")
  @CrossOrigin
  public ResponseEntity<Void> addArticleToUserFavorites(
      @PathVariable Long userId, @PathVariable Long articleId) {
    favouriteArticlesService.addArticleToUserFavorites(userId, articleId);
    return ResponseEntity.ok().build();
  }

  /**
   * Remove article from user favorites response entity.
   *
   * @param userId the user id
   * @param articleId the article id
   * @return the response entity
   */
  @DeleteMapping("/{userId}/remove/{articleId}")
  @CrossOrigin
  public ResponseEntity<Void> removeArticleFromUserFavorites(
      @PathVariable Long userId, @PathVariable Long articleId) {
    favouriteArticlesService.removeArticleFromUserFavorites(userId, articleId);
    return ResponseEntity.ok().build();
  }

  /**
   * Edit user favorite article response entity.
   *
   * @param userId the user id
   * @param prevArticleId the prev article id
   * @param newArticleId the new article id
   * @return the response entity
   */
  @PutMapping("/{userId}/edit/{prevArticleId}/{newArticleId}")
  @CrossOrigin
  public ResponseEntity<Void> editUserFavoriteArticle(
      @PathVariable Long userId,
      @PathVariable Long prevArticleId,
      @PathVariable Long newArticleId) {
    favouriteArticlesService.editUserFavoriteArticle(userId, prevArticleId, newArticleId);
    return ResponseEntity.ok().build();
  }

  /**
   * Gets articles saved by user.
   *
   * @param articleId the article id
   * @return the articles saved by user
   */
  @GetMapping("/{articleId}/get-saved-users")
  @CrossOrigin
  public ResponseEntity<Set<User>> getArticlesSavedByUser(@PathVariable Long articleId) {
    Set<User> savedUsers = favouriteArticlesService.getArticlesSavedByUser(articleId);
    return ResponseEntity.ok(savedUsers);
  }

  /**
   * Gets user favorite articles.
   *
   * @param userId the user id
   * @return the user favorite articles
   */
  @GetMapping("/{userId}/get-favorite-articles")
  @CrossOrigin
  public ResponseEntity<FavouriteArticlesDto> getUserFavoriteArticles(@PathVariable Long userId) {
    FavouriteArticlesDto articles = favouriteArticlesService.getUserFavoriteArticles(userId);
    return ResponseEntity.ok(articles);
  }
}
