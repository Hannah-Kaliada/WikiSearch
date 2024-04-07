package com.search.wiki.controller;

import com.search.wiki.entity.Country;
import com.search.wiki.service.CountryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<List<Country>> getAllCountries() {
    List<Country> countries = countryService.getAllCountries();
    return ResponseEntity.ok(countries);
  }

  /**
   * Gets country by id.
   *
   * @param id the id
   * @return the country by id
   */
  @GetMapping("/{id}")
  public ResponseEntity<Country> getCountryById(@PathVariable long id) {
    Country country = countryService.getCountryById(id);
    return country != null ? ResponseEntity.ok(country) : ResponseEntity.notFound().build();
  }

  /**
   * Add country response entity.
   *
   * @param country the country
   * @return the response entity
   */
  @PostMapping("/addCountry")
  public ResponseEntity<Country> addCountry(@Valid @RequestBody Country country) {
    Country addedCountry = countryService.addCountry(country);
    return ResponseEntity.status(HttpStatus.CREATED).body(addedCountry);
  }

  /**
   * Update country response entity.
   *
   * @param id the id
   * @param country the country
   * @return the response entity
   */
  @PutMapping("/updateCountry/{id}")
  public ResponseEntity<Country> updateCountry(
      @PathVariable long id, @Valid @RequestBody Country country) {
    Country updatedCountry = countryService.updateCountry(country, id);
    if (updatedCountry != null) {
      return ResponseEntity.ok(updatedCountry);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Delete country response entity.
   *
   * @param id the id
   * @return the response entity
   */
  @DeleteMapping("/deleteCountry/{id}")
  public ResponseEntity<Boolean> deleteCountry(@PathVariable long id) {
    boolean deleted = countryService.deleteCountry(id);
    return ResponseEntity.ok(deleted);
  }
}
