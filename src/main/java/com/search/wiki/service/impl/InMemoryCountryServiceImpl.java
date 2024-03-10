package com.search.wiki.service.impl;

import com.search.wiki.entity.Country;
import com.search.wiki.repository.CountryDAO;
import com.search.wiki.service.CountryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InMemoryCountryServiceImpl implements CountryService {

    private final CountryDAO repository;

    public InMemoryCountryServiceImpl(CountryDAO repository) {
        this.repository = repository;
    }

    @Override
    public Country addCountry(Country country) {
        return repository.saveCountry(country).orElse(null);
    }

    @Override
    public Country getCountryById(long id) {
        return repository.findById(id);
    }

    @Override
    public Country getCountryByName(String name) {
        return repository.findByName(name).orElse(null);
    }

    @Override
    public Country updateCountry(Country country) {
        return repository.updateCountry(country);
    }

    @Override
    public boolean deleteCountry(long id) {
        return repository.deleteCountry(id);
    }

    @Override
    public List<Country> getAllCountries() {
        return repository.findAllCountries();
    }
}
