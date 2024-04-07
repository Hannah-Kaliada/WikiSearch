package com.search.wiki.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Data;

/** The type Favourite articles dto. */
@Data
public class FavouriteArticlesDto {
  @NotBlank(message = "Id is mandatory")
  private Long userId;

  @NotBlank(message = "Username is mandatory")
  @Size(max = 255, message = "Username cannot exceed 255 characters")
  private String username;

  @Size(max = 255, message = "Email cannot exceed 255 characters")
  @NotBlank(message = "Email is mandatory")
  private String email;

  private CountryDto country;

  @NotBlank(message = "Password is mandatory")
  @Size(max = 255, message = "Password cannot exceed 255 characters")
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
