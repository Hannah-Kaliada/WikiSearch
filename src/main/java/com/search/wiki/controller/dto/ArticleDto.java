package com.search.wiki.controller.dto;

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

  // Дополнительно можно переопределить методы equals() и hashCode(), если это необходимо для вашего
  // использования

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ArticleDto that = (ArticleDto) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (title != null ? !title.equals(that.title) : that.title != null) return false;
    if (url != null ? !url.equals(that.url) : that.url != null) return false;
    return imagePath != null ? imagePath.equals(that.imagePath) : that.imagePath == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (url != null ? url.hashCode() : 0);
    result = 31 * result + (imagePath != null ? imagePath.hashCode() : 0);
    return result;
  }
}
