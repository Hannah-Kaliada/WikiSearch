package com.search.wiki.controller;

import com.search.wiki.entity.User;
import com.search.wiki.service.FavouriteArticlesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/favorite-articles")
@AllArgsConstructor
public class FavouriteArticlesController {

    private final FavouriteArticlesService favouriteArticlesService;

    @PostMapping("/{userId}/add/{articleId}")
    public ResponseEntity<Void> addArticleToUserFavorites(@PathVariable Long userId, @PathVariable Long articleId) {
        favouriteArticlesService.addArticleToUserFavorites(userId, articleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/remove/{articleId}")
    public ResponseEntity<Void> removeArticleFromUserFavorites(@PathVariable Long userId, @PathVariable Long articleId) {
        favouriteArticlesService.removeArticleFromUserFavorites(userId, articleId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/edit/{prevArticleId}/{newArticleId}")
    public ResponseEntity<Void> editUserFavoriteArticle(@PathVariable Long userId,
                                                        @PathVariable Long prevArticleId,
                                                        @PathVariable Long newArticleId) {
        favouriteArticlesService.editUserFavoriteArticle(userId, prevArticleId, newArticleId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{articleId}/add-favorite/{userId}")
    public ResponseEntity<Void> addFavoriteUserToArticle(@PathVariable Long articleId, @PathVariable Long userId) {
        favouriteArticlesService.addFavoriteUserToArticle(articleId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{articleId}/remove-favorite/{userId}")
    public ResponseEntity<Void> removeFavoriteUserFromArticle(@PathVariable Long articleId, @PathVariable Long userId) {
        favouriteArticlesService.removeFavoriteUserFromArticle(articleId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{articleId}/get-saved-users")
    public ResponseEntity<Set<User>> getArticlesSavedByUser(@PathVariable Long articleId) {
        Set<User> savedUsers = favouriteArticlesService.getArticlesSavedByUser(articleId);
        return ResponseEntity.ok(savedUsers);
    }
}
