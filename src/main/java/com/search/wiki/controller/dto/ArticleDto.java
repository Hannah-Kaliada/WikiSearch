package com.search.wiki.controller.dto;

import java.util.Objects;

public class ArticleDto {

  private Long id;
  private String title;
  private String url;
  private String imagePath;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ArticleDto articleDto = (ArticleDto) o;
    return Objects.equals(id, articleDto.id)
        && Objects.equals(title, articleDto.title)
        && Objects.equals(url, articleDto.url)
        && Objects.equals(imagePath, articleDto.imagePath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, url, imagePath);
  }
}
