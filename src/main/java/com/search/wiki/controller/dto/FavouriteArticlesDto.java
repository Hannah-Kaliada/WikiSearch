package com.search.wiki.controller.dto;

import java.util.Set;
import lombok.Data;

/** The type Favourite articles dto. */
@Data
public class FavouriteArticlesDto {
  private Long userId;
  private String username;
  private String email;
  private CountryDto country;
  private String password;
  private Set<ArticleDto> favouriteArticles;

  /**
   * Gets country.
   *
   * @return the country
   */
  public CountryDto getCountry() {
    return country;
  }
}
