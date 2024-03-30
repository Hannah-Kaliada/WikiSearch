package com.search.wiki.repository;

import com.search.wiki.entity.Article;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/** The interface Article repository. */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
  /**
   * Find top 5 articles by user count list.
   *
   * @param pageable the pageable
   * @return the list
   */
  @Query(
      "SELECT article "
          + "FROM Article article "
          + "LEFT JOIN article.users user "
          + "GROUP BY article "
          + "ORDER BY COUNT(user) DESC")
  List<Article> findTop5ArticlesByUserCount(Pageable pageable);

  /**
   * Find by title containing list.
   *
   * @param keyword the keyword
   * @return the list
   */
  @Query("SELECT a FROM Article a WHERE LOWER(a.title) LIKE LOWER(concat('%', :keyword, '%'))")
  List<Article> findByTitleContaining(String keyword);

  /**
   * Exists by title boolean.
   *
   * @param title the title
   * @return the boolean
   */
  boolean existsByTitle(String title);
}
