package com.search.wiki.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** The type Article dto. */
@Data
public class ArticleDto {
  @NotBlank(message = "Id is mandatory")
  private Long id;
  @NotBlank(message = "Title cannot be blank")
  @Size(max = 255, message = "Title cannot exceed 255 characters")
  private String title;
  @NotBlank(message = "Url is mandatory")
  @Size(max = 255, message = "Url cannot exceed 255 characters")
  private String url;
  private String imagePath;
}
