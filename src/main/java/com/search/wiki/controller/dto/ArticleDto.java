package com.search.wiki.controller.dto;

import lombok.Data;

/** The type Article dto. */
@Data
public class ArticleDto {
  private Long id;
  private String title;
  private String url;
  private String imagePath;
}
