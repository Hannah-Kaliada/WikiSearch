package com.search.wiki.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** The type Country dto. */
@Data
public class CountryDto {
  @NotBlank(message = "Id is mandatory")
  private Long id;
  @NotBlank(message = "Name is mandatory")
  @Size(max = 255, message = "Name cannot exceed 255 characters")
  @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Name should contain only alphabets and spaces")
  private String name;

  /**
   * Gets name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public Long getId() {
    return id;
  }
}
