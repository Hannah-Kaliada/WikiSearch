package com.search.wiki.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(unique = true)
  private String username;

  @Column(unique = true)
  private String email;

  private String password;

  @ManyToOne
  @JoinColumn(name = "country_id")
  @JsonBackReference
  private Country country;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_favorite_articles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "article_id"))
  private Set<Article> favoriteArticles = new HashSet<>();

  public User() {}

  public User(String username, String email, String password, Country country) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.country = country;
  }

  public User(long id, String username, String email, String password, Country country) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.country = country;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Country getCountry() {
    return country;
  }

  public void setCountry(Country country) {
    this.country = country;
  }

  public Set<Article> getFavoriteArticles() {
    return favoriteArticles;
  }

  public void setFavoriteArticles(Set<Article> favoriteArticles) {
    this.favoriteArticles = favoriteArticles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return id == user.getId()
        && Objects.equals(username, user.getUsername())
        && Objects.equals(email, user.getEmail())
        && Objects.equals(password, user.getPassword())
        && Objects.equals(country, user.getCountry());
  }


  @Override
  public int hashCode() {
    return Objects.hash(id, username, email, password, country);
  }

}
