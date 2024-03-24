package com.search.wiki.repository;

import com.search.wiki.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT article " +
            "FROM Article article " +
            "LEFT JOIN article.users user " +
            "GROUP BY article " +
            "ORDER BY COUNT(user) DESC")
    List<Article> findTop5ArticlesByUserCount(Pageable pageable);

}
