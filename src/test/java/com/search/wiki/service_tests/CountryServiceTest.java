package com.search.wiki.service_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.search.wiki.cache.Cache;
import com.search.wiki.entity.Country;
import com.search.wiki.exceptions.customexceptions.DuplicateEntryException;
import com.search.wiki.exceptions.customexceptions.NotFoundException;
import com.search.wiki.repository.CountryRepository;
import com.search.wiki.service.CountryService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** The type Country service tests. */
@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

  @Mock private CountryRepository countryRepository;

  @Mock private Cache cache;

  @InjectMocks private CountryService countryService;

  /** Test add country success. */
  @Test
  void testAddCountry_Success() {
    Country inputCountry = new Country();
    inputCountry.setName("Test Country");

    when(countryRepository.findByName(any())).thenReturn(Optional.empty());
    when(countryRepository.save(any())).thenReturn(inputCountry);

    Country savedCountry = countryService.addCountry(inputCountry);

    assertNotNull(savedCountry);
    assertEquals("Test Country", savedCountry.getName());
    verify(countryRepository, times(1)).save(inputCountry);
    verify(cache, times(1)).put(any(), any());
  }

  /** Test add country duplicate name. */
  @Test
  void testAddCountry_DuplicateName() {
    Country inputCountry = new Country();
    inputCountry.setName("Existing Country");

    when(countryRepository.findByName(any())).thenReturn(Optional.of(new Country()));

    assertThrows(DuplicateEntryException.class, () -> countryService.addCountry(inputCountry));
  }

  /** Test get country by id existing id. */
  @Test
  void testGetCountryById_ExistingId() {
    long countryId = 1L;
    Country existingCountry = new Country();
    existingCountry.setId(countryId);
    existingCountry.setName("Test Country");

    when(countryRepository.findById(countryId)).thenReturn(Optional.of(existingCountry));

    Country retrievedCountry = countryService.getCountryById(countryId);

    assertNotNull(retrievedCountry);
    assertEquals(countryId, retrievedCountry.getId());
    assertEquals("Test Country", retrievedCountry.getName());
  }

  /** Test get country by id nonexistent id. */
  @Test
  void testGetCountryById_NonexistentId() {
    long nonexistentId = 999L;

    when(countryRepository.findById(nonexistentId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> countryService.getCountryById(nonexistentId));
  }

  /** Test update country success. */
  @Test
  void testUpdateCountry_Success() {
    long countryId = 1L;
    Country existingCountry = new Country();
    existingCountry.setId(countryId);
    existingCountry.setName("Existing Country");

    Country updatedCountryData = new Country();
    updatedCountryData.setName("Updated Country");

    when(countryRepository.findById(countryId)).thenReturn(Optional.of(existingCountry));
    when(countryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    Country updatedCountry = countryService.updateCountry(updatedCountryData, countryId);

    assertNotNull(updatedCountry);
    assertEquals(countryId, updatedCountry.getId());
    assertEquals("Updated Country", updatedCountry.getName());
  }

  /** Test update country nonexistent id. */
  @Test
  void testUpdateCountry_NonexistentId() {
    long nonexistentId = 999L;
    Country updatedCountryData = new Country();
    updatedCountryData.setName("Updated Country");

    when(countryRepository.findById(nonexistentId)).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class,
        () -> countryService.updateCountry(updatedCountryData, nonexistentId));
  }

  /** Test delete country success. */
  @Test
  void testDeleteCountry_Success() {
    long countryId = 1L;
    Country existingCountry = new Country();
    existingCountry.setId(countryId);

    when(countryRepository.findById(countryId)).thenReturn(Optional.of(existingCountry));

    doNothing().when(countryRepository).deleteById(countryId);

    boolean result = countryService.deleteCountry(countryId);

    assertTrue(result);
    verify(countryRepository, times(1)).deleteById(countryId);
    verify(cache, times(1)).remove(any());
  }

  /** Test delete country nonexistent id. */
  @Test
  void testDeleteCountry_NonexistentId() {
    long nonexistentId = 999L;

    when(countryRepository.findById(nonexistentId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> countryService.deleteCountry(nonexistentId));
  }

  /** Test add country null input. */
  @Test
  void testAddCountry_NullInput() {
    assertThrows(IllegalArgumentException.class, () -> countryService.addCountry(null));
  }

  /** Test get all countries cache hit. */
  @Test
  void testGetAllCountries_CacheHit() {

    when(cache.containsKey(any())).thenReturn(true);
    List<Country> cachedCountries = new ArrayList<>();
    when(cache.get(any())).thenReturn(cachedCountries);

    List<Country> result = countryService.getAllCountries();

    assertNotNull(result);
    assertEquals(cachedCountries, result);
  }

  /** Test get all countries empty repository. */
  @Test
  void testGetAllCountries_EmptyRepository() {

    when(countryRepository.findAll()).thenReturn(Collections.emptyList());

    assertThrows(NotFoundException.class, () -> countryService.getAllCountries());
  }

  /** Test get country id by name existing name. */
  @Test
  void testGetCountryIdByName_ExistingName() {
    String existingCountryName = "Existing Country";
    long countryId = 1L;
    Country existingCountry = new Country();
    existingCountry.setId(countryId);

    when(countryRepository.findByName(existingCountryName))
        .thenReturn(Optional.of(existingCountry));

    Long result = countryService.getCountryIdByName(existingCountryName);

    assertNotNull(result);
    assertEquals(countryId, result);
  }

  /** Test get country id by name nonexistent name. */
  @Test
  void testGetCountryIdByName_NonexistentName() {
    String nonexistentCountryName = "Nonexistent Country";

    when(countryRepository.findByName(nonexistentCountryName)).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class, () -> countryService.getCountryIdByName(nonexistentCountryName));
  }

  /** Test add country and get id null country. */
  @Test
  void testAddCountryAndGetId_NullCountry() {
    assertThrows(IllegalArgumentException.class, () -> countryService.addCountryAndGetId(null));
  }

  /** Test add country and get id duplicate name. */
  @Test
  void testAddCountryAndGetId_DuplicateName() {
    Country existingCountry = new Country();
    existingCountry.setName("Existing Country");

    when(countryRepository.findByName(existingCountry.getName()))
        .thenReturn(Optional.of(existingCountry));

    assertThrows(
        DuplicateEntryException.class, () -> countryService.addCountryAndGetId(existingCountry));
  }

  /** Test add country and get id success. */
  @Test
  void testAddCountryAndGetId_Success() {
    Country newCountry = new Country();
    newCountry.setName("New Country");

    when(countryRepository.findByName(newCountry.getName())).thenReturn(Optional.empty());
    when(countryRepository.save(any()))
        .thenAnswer(
            invocation -> {
              Country savedCountry = invocation.getArgument(0);
              savedCountry.setId(1L);
              return savedCountry;
            });

    Long countryId = countryService.addCountryAndGetId(newCountry);

    assertNotNull(countryId);
    assertEquals(1L, countryId);
  }
}
