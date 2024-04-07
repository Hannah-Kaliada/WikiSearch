package com.search.wiki.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** The type User dto. */
@Data
public class UserDto {
  @NotBlank(message = "Id is mandatory")
  private Long id;
  @NotBlank(message = "Username is mandatory")
  @Size(max = 255, message = "Username cannot exceed 255 characters")
  private String username;
  @Email(message = "Email should be valid")
  private String email;
  private CountryDto country;
  @NotBlank(message = "Password is mandatory")
  @Size(max = 255, message = "Password cannot exceed 255 characters")
  private String password;
}
