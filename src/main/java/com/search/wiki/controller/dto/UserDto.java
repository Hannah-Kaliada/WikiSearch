package com.search.wiki.controller.dto;

import java.util.Objects;

public class UserDto {
  private Long id;
  private String username;
  private String email;
  private CountryDto country;
  private String password;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public CountryDto getCountry() {
    return country;
  }

  public void setCountry(CountryDto country) {
    this.country = country;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserDto userDto = (UserDto) o;
    return Objects.equals(id, userDto.id) &&
            Objects.equals(username, userDto.username) &&
            Objects.equals(email, userDto.email) &&
            Objects.equals(country, userDto.country) &&
            Objects.equals(password, userDto.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, email, country, password);
  }
}
