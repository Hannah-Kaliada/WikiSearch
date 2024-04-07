package com.search.wiki.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.search.wiki.cache.Cache;
import com.search.wiki.entity.Country;
import com.search.wiki.exceptions.customexceptions.DuplicateEntryException;
import com.search.wiki.exceptions.customexceptions.NotFoundException;
import com.search.wiki.repository.CountryRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** The type Country service tests. */
@ExtendWith(MockitoExtension.class)
class CountryServiceTests {

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

    // Call the method to be tested
    Country savedCountry = countryService.addCountry(inputCountry);

    // Verify that country was saved correctly
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
}
