package com.search.wiki.service;

import com.search.wiki.cache.Cache;
import com.search.wiki.entity.Country;
import com.search.wiki.exceptions.ExceptionConstants;
import com.search.wiki.exceptions.customexceptions.DuplicateEntryException;
import com.search.wiki.exceptions.customexceptions.NotFoundException;
import com.search.wiki.repository.CountryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** The type Country service. */
@Service
public class CountryService {

  private final CountryRepository repository;
  private final Cache cache;
  private static final String COUNTRY_CACHE_PREFIX = "Country_";

  /**
   * Instantiates a new Country service.
   *
   * @param repository the repository
   * @param cache the cache
   */
  public CountryService(CountryRepository repository, Cache cache) {
    this.repository = repository;
    this.cache = cache;
  }

  /**
   * Add country.
   *
   * @param country the country
   * @return the country
   */
  public Country addCountry(Country country) {
    if (country == null) {
      throw new IllegalArgumentException("Country cannot be null");
    }
    if (repository.findByName(country.getName()).isPresent()) {
      throw new DuplicateEntryException("Country already exists with name: " + country.getName());
    }
    Country savedCountry = repository.save(country);
    cache.put(getCacheKey(savedCountry.getId()), savedCountry);
    return savedCountry;
  }

  /**
   * Gets country by id.
   *
   * @param id the id
   * @return the country by id
   */
  public Country getCountryById(long id) {
    if (id < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
    }
    String cacheKey = getCacheKey(id);
    return getCachedOrFromRepository(cacheKey, id);
  }

  /**
   * Update country.
   *
   * @param country the country
   * @param id the id
   * @return the country
   */
  public Country updateCountry(Country country, Long id) {
    if (id < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
    }
    Optional<Country> optionalCountry = repository.findById(id);
    if (optionalCountry.isPresent()) {
      Country existingCountry = optionalCountry.get();
      existingCountry.setName(country.getName());
      Country updatedCountry = repository.save(existingCountry);
      String cacheKey = getCacheKey(updatedCountry.getId());
      cache.put(cacheKey, updatedCountry);
      return updatedCountry;
    } else {
      throw new NotFoundException(ExceptionConstants.COUNTRY_NOT_FOUND + id);
    }
  }

  /**
   * Delete country boolean.
   *
   * @param countryId the country id
   * @return the boolean
   */
  @Transactional
  public boolean deleteCountry(long countryId) {
    if (countryId < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
    }
    Optional<Country> countryOptional = repository.findById(countryId);
    if (countryOptional.isPresent()) {
      Country country = countryOptional.get();
      country.getUsers().forEach(user -> user.setCountry(null));
      country.getUsers().clear();
      repository.deleteById(countryId);
      cache.remove(getCacheKey(countryId));
      return true;
    }
    throw new NotFoundException(ExceptionConstants.COUNTRY_NOT_FOUND + countryId);
  }

  /**
   * Gets all countries.
   *
   * @return the all countries
   */
  public List<Country> getAllCountries() {
    String cacheKey = COUNTRY_CACHE_PREFIX + "AllCountries";
    if (cache.containsKey(cacheKey)) {
      return (List<Country>) cache.get(cacheKey);
    } else {
      List<Country> countries = repository.findAll();
      if (countries.isEmpty()) {
        throw new NotFoundException("No countries found");
      }
      countries.forEach(country -> cache.put(getCacheKey(country.getId()), country));
      cache.put(cacheKey, countries);
      return countries;
    }
  }

  private String getCacheKey(long id) {
    return COUNTRY_CACHE_PREFIX + id;
  }

  private Country getCachedOrFromRepository(String cacheKey, long id) {
    if (id < 1) {
      throw new IllegalArgumentException("Id cannot be less than 1");
    }

    if (cache.containsKey(cacheKey)) {
      return (Country) cache.get(cacheKey);
    } else {
      Optional<Country> countryOptional = repository.findById(id);
      if (countryOptional.isPresent()) {
        Country country = countryOptional.get();
        cache.put(cacheKey, country);
        return country;
      } else {
        throw new NotFoundException("Country not found with id: " + id);
      }
    }
  }



  /**
   * Gets country id by name.
   *
   * @param countryName the country name
   * @return the country id by name
   */
  public Long getCountryIdByName(String countryName) {
    return repository.findByName(countryName)
            .map(Country::getId)
            .orElseThrow(() -> new NotFoundException("Country not found with name: " + countryName));
  }

  /**
   * Add country and get id long.
   *
   * @param country the country
   * @return the long
   */
  public Long addCountryAndGetId(Country country) {
    if (country == null) {
      throw new IllegalArgumentException("Country cannot be null");
    }
    if (repository.findByName(country.getName()).isPresent()) {
      throw new DuplicateEntryException("Country already exists with name: " + country.getName());
    }
    repository.save(country);
    return country.getId();
  }
}
