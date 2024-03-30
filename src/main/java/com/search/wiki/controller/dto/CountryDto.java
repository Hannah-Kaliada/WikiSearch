package com.search.wiki.controller.dto;

import lombok.Data;

/** The type Country dto. */
@Data
public class CountryDto {
  private Long id;
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
