package com.search.wiki.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    @Column(unique = true)
    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "country_id")
    @JsonBackReference
    private Country country;

    @ManyToMany
    @JoinTable(
            name = "user_favorite_articles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id")

    )
    private Set<Article> favoriteArticles = new HashSet<>();

    public Set<Article> getFavoriteArticles() {
        return favoriteArticles;
    }

    public void setFavoriteArticles(Set<Article> favoriteArticles) {
        this.favoriteArticles = favoriteArticles;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
