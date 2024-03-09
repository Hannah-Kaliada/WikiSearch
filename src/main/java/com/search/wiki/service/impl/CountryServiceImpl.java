package com.search.wiki.service.impl;

import com.search.wiki.entity.Country;
import com.search.wiki.repository.CountryRepository;
import com.search.wiki.service.CountryService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public boolean deleteCountry(long countryId) {
        Country country = repository.findById(countryId).orElse(null);
        if (country == null) {
            return false; // Страна не найдена
        }

        // Разорвать связи между страной и пользователями
        country.getUsers().forEach(user -> user.setCountry(null));
        country.getUsers().clear();

        // Теперь можно удалить страну
        repository.deleteById(countryId);

        return true;
    }

    @Override
    public List<Country> getAllCountries() {
        return repository.findAll();
    }
}