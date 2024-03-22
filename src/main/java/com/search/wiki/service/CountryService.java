package com.search.wiki.service;

import com.search.wiki.entity.Country;
import com.search.wiki.repository.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CountryService {

    private final CountryRepository repository;

    public CountryService(CountryRepository repository) {
        this.repository = repository;
    }

    public Country addCountry(Country country) {
        return repository.save(country);
    }

    public Country getCountryById(long id) {
        return repository.findById(id).orElse(null);
    }

    public Country getCountryByName(String name) {
        return repository.findByName(name).orElse(null);
    }

    public Country updateCountry(Country country) {
        return repository.save(country);
    }

    @Transactional
    public boolean deleteCountry(long countryId) {
        Country country = repository.findById(countryId).orElse(null);
        if (country == null) {
            return false;
        }
        country.getUsers().forEach(user -> user.setCountry(null));
        country.getUsers().clear();
        repository.deleteById(countryId);
        return true;
    }

    public List<Country> getAllCountries() {
        return repository.findAll();
    }
}
