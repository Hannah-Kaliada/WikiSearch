package com.search.wiki.controller.dto;

import java.util.Set;

import lombok.Data;

@Data
public class FavouriteArticlesDTO {
    private Long userId;
    private String username;
    private String email;
    private CountryDTO country;
    private String password;
    private Set<ArticleDTO> favouriteArticles;
    public CountryDTO getCountry() {
        return country;
    }
}
