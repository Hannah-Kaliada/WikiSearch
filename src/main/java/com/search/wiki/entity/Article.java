package com.search.wiki.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

/** The type Article. */
@Getter
@Setter
@Entity
@Table(name = "articles")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Article article = (Article) o;

    return id == article.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public Article(String title, String url, String imagePath) {
    this.title = title;
    this.url = url;
    this.imagePath = imagePath;
  }
}
