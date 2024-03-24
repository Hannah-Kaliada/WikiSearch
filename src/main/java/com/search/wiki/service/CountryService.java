package com.search.wiki.service;

import com.search.wiki.cache.Cache;
import com.search.wiki.entity.Country;
import com.search.wiki.repository.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {

    private final CountryRepository repository;
    private final Cache cache;
    private static final String COUNTRY_CACHE_PREFIX = "Country_";

    public CountryService(CountryRepository repository, Cache cache) {
        this.repository = repository;
        this.cache = cache;
    }

    public Country addCountry(Country country) {
        Country savedCountry = repository.save(country);
        cache.put(getCacheKey(savedCountry.getId()), savedCountry);
        return savedCountry;
    }

    public Country getCountryById(long id) {
        String cacheKey = getCacheKey(id);
        return getCachedOrFromRepository(cacheKey, id);
    }

    public Country getCountryByName(String name) {
        Long id = getCountryIdByName(name);
        return id != null ? getCachedOrFromRepository(getCacheKey(id), id) : null;
    }

    public Country updateCountry(Country country, Long id) {
        Optional<Country> optionalCountry = repository.findById(id);
        if (optionalCountry.isPresent()) {
            Country existingCountry = optionalCountry.get();
            existingCountry.setName(country.getName());
            Country updatedCountry = repository.save(existingCountry);
            String cacheKey = getCacheKey(updatedCountry.getId());
            cache.put(cacheKey, updatedCountry);
            return updatedCountry;
        } else {
            return null;
        }
    }

    @Transactional
    public boolean deleteCountry(long countryId) {
        Optional<Country> countryOptional = repository.findById(countryId);
        if (countryOptional.isPresent()) {
            Country country = countryOptional.get();
            country.getUsers().forEach(user -> user.setCountry(null));
            country.getUsers().clear();
            repository.deleteById(countryId);
            cache.remove(getCacheKey(countryId));
            return true;
        }
        return false;
    }

    public List<Country> getAllCountries() {
        String cacheKey = COUNTRY_CACHE_PREFIX + "AllCountries";
        if (cache.containsKey(cacheKey)) {
            return (List<Country>) cache.get(cacheKey);
        } else {
            List<Country> countries = repository.findAll();
            for (Country country : countries) {
                String countryCacheKey = getCacheKey(country.getId());
                cache.put(countryCacheKey, country);
            }
            cache.put(cacheKey, countries);
            return countries;
        }
    }

    private String getCacheKey(long id) {
        return COUNTRY_CACHE_PREFIX + id;
    }

    private Country getCachedOrFromRepository(String cacheKey, long id) {
        if (cache.containsKey(cacheKey)) {
            return (Country) cache.get(cacheKey);
        } else {
            Optional<Country> countryOptional = repository.findById(id);
            if (countryOptional.isPresent()) {
                Country country = countryOptional.get();
                cache.put(cacheKey, country);
                return country;
            }
            return null;
        }
    }

    public Long getCountryIdByName(String countryName) {
        Optional<Country> optionalCountry = repository.findByName(countryName);
        return optionalCountry.map(Country::getId).orElse(null);
    }

    public Long addCountryAndGetId(Country country) {
        repository.save(country);
        return country.getId();
    }
}
