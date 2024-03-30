package com.search.wiki.controller.dto;

import lombok.Data;

/** The type User dto. */
@Data
public class UserDto {
  private Long id;
  private String username;
  private String email;
  private CountryDto country;
  private String password;
}
