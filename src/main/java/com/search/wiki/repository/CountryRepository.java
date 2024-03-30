package com.search.wiki.repository;

import com.search.wiki.entity.Country;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** The interface Country repository. */
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
  /**
   * Find by name optional.
   *
   * @param name the name
   * @return the optional
   */
  Optional<Country> findByName(String name);
}
