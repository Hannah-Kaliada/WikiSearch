package com.search.wiki.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
@Entity
@Table(name = "articles")
@DynamicUpdate
public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(unique = true)
  private String title;

  private String url;
  private String imagePath;

  @ManyToMany(mappedBy = "favoriteArticles")
  @JsonBackReference
  private Set<User> users = new HashSet<>();

  public Article() {
    // Пустой конструктор
  }

  public Article(String title, String url, String imagePath) {
    this.title = title;
    this.url = url;
    this.imagePath = imagePath;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
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

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Article article = (Article) o;

    return id == article.id &&
            Objects.equals(title, article.title) &&
            Objects.equals(url, article.url) &&
            Objects.equals(imagePath, article.imagePath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, url, imagePath);
  }
}
