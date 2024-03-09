package com.search.wiki.service;

import com.search.wiki.model.Country;

import java.util.List;

public interface CountryService {
    Country addCountry(Country country);

    Country getCountryById(long id);

    Country updateCountry(Country country);

    boolean deleteCountry(long id);

    List<Country> getAllCountries();
    Country getCountryByName(String name);
}
