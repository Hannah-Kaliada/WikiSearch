package com.search.wiki.repository;

import com.search.wiki.entity.Country;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CountryDAO {

    private final List<Country> countries = new ArrayList<>();

    public List<Country> findAllCountries() {
        return countries;
    }

    public Optional<Country> saveCountry(Country country) {
        boolean countryExists = countries.stream().anyMatch(c -> c.getName().equals(country.getName()));

        if (!countryExists) {
            countries.add(country);
            return Optional.of(country);
        } else {
            return Optional.empty();
        }
    }

    public Country findById(long id) {
        return countries.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public Country updateCountry(Country country) {
        Optional<Country> existingCountry = countries.stream().filter(c -> c.getId() == country.getId()).findFirst();
        existingCountry.ifPresent(c -> c.setName(country.getName()));
        return country;
    }

    public boolean deleteCountry(long id) {
        return countries.removeIf(country -> country.getId() == id);
    }
    public Optional<Country> findByName(String name) {
        return countries.stream()
                .filter(country -> country.getName().equals(name))
                .findFirst();
    }
}
