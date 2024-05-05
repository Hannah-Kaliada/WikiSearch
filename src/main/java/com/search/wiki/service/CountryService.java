package com.search.wiki.service;

import com.search.wiki.cache.Cache;
import com.search.wiki.entity.Country;
import com.search.wiki.exceptions.ExceptionConstants;
import com.search.wiki.exceptions.customexceptions.DuplicateEntryException;
import com.search.wiki.exceptions.customexceptions.NotFoundException;
import com.search.wiki.repository.CountryRepository;
import com.search.wiki.service.utils.RequestCountService;
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
  private final RequestCountService requestCountService;

  /**
   * Instantiates a new Country service.
   *
   * @param repository the repository
   * @param cache the cache
   * @param requestCountService the request count service
   */
public CountryService(
      CountryRepository repository, Cache cache, RequestCountService requestCountService) {
    this.repository = repository;
    this.cache = cache;
    this.requestCountService = requestCountService;
  }

  /**
   * Add country.
   *
   * @param country the country
   * @return the country
   */
public Country addCountry(Country country) {
    requestCountService.incrementRequestCount();
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
    requestCountService.incrementRequestCount();
    if (id < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
    }
    String cacheKey = getCacheKey(id);
    return getCachedOrFromRepository(cacheKey, id);
  }

  public Country getCountryByName(String name) {
    requestCountService.incrementRequestCount();
    return repository.findByName(name)
            .orElseThrow(() -> new NotFoundException("Country not found with name: " + name));
  }

  /**
   * Update country.
   *
   * @param country the country
   * @param id the id
   * @return the country
   */
public Country updateCountry(Country country, Long id) {
    requestCountService.incrementRequestCount();
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
    requestCountService.incrementRequestCount();
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
    requestCountService.incrementRequestCount();
    return repository.findAll();
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
    requestCountService.incrementRequestCount();
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
    requestCountService.incrementRequestCount();
    if (country == null) {
      throw new IllegalArgumentException("Country cannot be null");
    }
    if (repository.findByName(country.getName()).isPresent()) {
      throw new DuplicateEntryException("Country already exists with name: " + country.getName());
    }
    repository.save(country);
    return country.getId();
  }

  /**
   * Bulk add countries.
   *
   * @param countryNames the country names
   */
@Transactional
  public void bulkAddCountries(List<String> countryNames) {
    requestCountService.incrementRequestCount();
    countryNames.stream()
        .map(
            countryName -> {
              Country country = new Country();
              country.setName(countryName);
              return country;
            })
        .forEach(repository::save);
  }

  /**
   * Gets request count.
   *
   * @return the request count
   */
  public int getRequestCount() {
    return requestCountService.getRequestCount();
  }
}
