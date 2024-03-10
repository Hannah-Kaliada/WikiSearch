package com.search.wiki.controller.dto;

import java.util.Set;

import lombok.Data;

@Data
public class FavouriteArticlesDTO {
    private Long userId;
    private Set<ArticleDTO> favouriteArticles;
}
