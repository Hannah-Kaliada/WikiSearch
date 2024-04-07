package com.search.wiki.controller;

import com.search.wiki.entity.Country;
import com.search.wiki.service.CountryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The type Country controller. */
@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

  private final CountryService countryService;

  /**
   * Instantiates a new Country controller.
   *
   * @param countryService the country service
   */
  @Autowired
  public CountryController(CountryService countryService) {
    this.countryService = countryService;
  }

  /**
   * Gets all countries.
   *
   * @return the all countries
   */
  @GetMapping
  public List<Country> getAllCountries() {
    return countryService.getAllCountries();
  }

  /**
   * Gets country by id.
   *
   * @param id the id
   * @return the country by id
   */
  @GetMapping("/{id}")
  public Country getCountryById(@PathVariable long id) {
    return countryService.getCountryById(id);
  }

  /**
   * Add country country.
   *
   * @param country the country
   * @return the country
   */
  @PostMapping("/addCountry")
  public Country addCountry(@Valid @RequestBody Country country) {
    return countryService.addCountry(country);
  }

  /**
   * Update  country.
   *
   * @param id the id
   * @param country the country
   * @return the country
   */
  @PutMapping("/updateCountry/{id}")
  public Country updateCountry(@PathVariable long id, @Valid @RequestBody Country country) {
    return countryService.updateCountry(country, id);
  }

  /**
   * Delete country boolean.
   *
   * @param id the id
   * @return the boolean
   */
  @DeleteMapping("/deleteCountry/{id}")
  public boolean deleteCountry(@PathVariable long id) {

    return countryService.deleteCountry(id);
  }

  /**
   * Gets country by name.
   *
   * @param name the name
   * @return the country by name
   */
  @GetMapping("/name/{name}")
  public Country getCountryByName(@PathVariable String name) {
    return countryService.getCountryByName(name);
  }
}
