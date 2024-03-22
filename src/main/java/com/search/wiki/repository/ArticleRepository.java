package com.search.wiki.repository;

import com.search.wiki.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a, COUNT(u) AS userCount " +
            "FROM Article a " +
            "LEFT JOIN a.users u " +
            "GROUP BY a " +
            "ORDER BY userCount DESC")
    List<Object[]> findTop5ArticlesByUserCount();

}
