package com.search.wiki.controller;

import com.search.wiki.entity.Country;
import com.search.wiki.service.CountryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** The type Country controller. */
@RestController
@RequestMapping("/api/v1/countries")
@CrossOrigin
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
@CrossOrigin
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
@CrossOrigin
  public ResponseEntity<Country> getCountryById(@PathVariable long id) {
    Country country = countryService.getCountryById(id);
    return country != null ? ResponseEntity.ok(country) : ResponseEntity.notFound().build();
  }

  @GetMapping("name/{name}")
  @CrossOrigin
  public ResponseEntity<Country> getCountryByName(@PathVariable String name) {
    Country country = countryService.getCountryByName(name);
    return country != null ? ResponseEntity.ok(country) : ResponseEntity.notFound().build();
  }


  /**
   * Add country response entity.
   *
   * @param country the country
   * @return the response entity
   */
@PostMapping("/addCountry")
@CrossOrigin
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
@CrossOrigin
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
@CrossOrigin
  public ResponseEntity<Boolean> deleteCountry(@PathVariable long id) {
    if (id < 1) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }
    boolean deleted = countryService.deleteCountry(id);
    if (deleted) {
      return ResponseEntity.ok(true);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
    }
  }

  /**
   * Bulk add countries response entity.
   *
   * @param countryNames the country names
   * @return the response entity
   */
@PostMapping("/addCountries")
@CrossOrigin
  public ResponseEntity<String> bulkAddCountries(@RequestBody List<String> countryNames) {
    if (countryNames == null || countryNames.isEmpty()) {
      return ResponseEntity.badRequest().body("List of country names is empty.");
    }
    countryService.bulkAddCountries(countryNames);
    return ResponseEntity.status(HttpStatus.CREATED).body("Bulk countries added successfully!");
  }

  /**
   * Gets count.
   *
   * @return the count
   */
  @GetMapping("/count")
  @CrossOrigin
  public int getCount() {
    // Получение количества обращений
    return countryService.getRequestCount();
  }
}
