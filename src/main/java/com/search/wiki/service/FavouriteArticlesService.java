package com.search.wiki.service;

import com.search.wiki.controller.dto.FavouriteArticlesDTO;

import java.util.Set;

public interface FavouriteArticlesService {
    void addArticleToUserFavorites(Long userId, Long articleId);

    void removeArticleFromUserFavorites(Long userId, Long articleId);

    public void editUserFavoriteArticle(Long userId, Long prevArticleId, Long newArticleId);

    void addFavoriteUserToArticle(Long articleId, Long userId);

    void removeFavoriteUserFromArticle(Long articleId, Long userId);

    FavouriteArticlesDTO getUserFavoriteArticles(Long userId);

    Set<Long> getArticlesSavedByUser(Long articleId);
}
