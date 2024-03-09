package com.search.wiki.service.impl;

import com.search.wiki.model.Country;
import com.search.wiki.repository.CountryRepository;
import com.search.wiki.service.CountryService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Primary
public class CountryServiceImpl implements CountryService {

    private final CountryRepository repository;

    @Override
    public Country addCountry(Country country) {
        return repository.save(country);
    }

    @Override
    public Country getCountryById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Country getCountryByName(String name) {
        return repository.findByName(name).orElse(null);
    }

    @Override
    public Country updateCountry(Country country) {
        return repository.save(country);
    }

    @Override
    public boolean deleteCountry(long id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Country> getAllCountries() {
        return repository.findAll();
    }
}