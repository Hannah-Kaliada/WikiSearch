package com.search.wiki.controller.dto;

import java.util.Objects;
import java.util.Set;

/** The type Favourite articles dto. */
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

  public void setCountry(CountryDto country) {
    this.country = country;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setFavouriteArticles(Set<ArticleDto> favouriteArticles) {
    this.favouriteArticles = favouriteArticles;
  }

  public Long getUserId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public Set<ArticleDto> getFavouriteArticles() {
    return favouriteArticles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FavouriteArticlesDto that = (FavouriteArticlesDto) o;
    return Objects.equals(userId, that.userId)
        && Objects.equals(username, that.username)
        && Objects.equals(email, that.email)
        && Objects.equals(country, that.country)
        && Objects.equals(password, that.password)
        && Objects.equals(favouriteArticles, that.favouriteArticles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, username, email, country, password, favouriteArticles);
  }
}
