package com.search.wiki.controller_tests;

import com.search.wiki.controller.CountryController;
import com.search.wiki.entity.Country;
import com.search.wiki.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryControllerTest {

		@Mock
		private CountryService countryService;

		@InjectMocks
		private CountryController countryController;

		private Validator validator;

		@BeforeEach
		void setUp() {
				MockitoAnnotations.initMocks(this);
				ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
				validator = factory.getValidator();
		}

		@Test
		void testGetAllCountries() {
				List<Country> mockCountries = new ArrayList<>();

				Country country1 = new Country();
				country1.setId(1L);
				country1.setName("USA");
				mockCountries.add(country1);

				Country country2 = new Country();
				country2.setId(2L);
				country2.setName("Canada");
				mockCountries.add(country2);


				// Mock service method
				when(countryService.getAllCountries()).thenReturn(mockCountries);

				// Call controller method
				ResponseEntity<List<Country>> responseEntity = countryController.getAllCountries();

				// Assertions
				assertNotNull(responseEntity);
				assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
				assertEquals(mockCountries, responseEntity.getBody());
		}

		@Test
		void testGetCountryById() {
				long countryId = 1L;
				Country mockCountry = new Country();
				mockCountry.setId(countryId);
				mockCountry.setName("USA");

				// Mock service method
				when(countryService.getCountryById(countryId)).thenReturn(mockCountry);

				// Call controller method with valid ID
				ResponseEntity<Country> responseEntity = countryController.getCountryById(countryId);

				// Assertions
				assertNotNull(responseEntity);
				assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
				assertEquals(mockCountry, responseEntity.getBody());

				// Call controller method with invalid ID
				responseEntity = countryController.getCountryById(999L);

				// Assertions for not found
				assertNotNull(responseEntity);
				assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
				assertNull(responseEntity.getBody());
		}

		@Test
		void testAddCountry() {
				// Prepare data
				Country countryToAdd = new Country();
				countryToAdd.setName("Germany");

				// Mock service behavior
				when(countryService.addCountry(any(Country.class))).thenReturn(countryToAdd);

				// Call controller method
				ResponseEntity<Country> responseEntity = countryController.addCountry(countryToAdd);

				// Assertions
				assertNotNull(responseEntity);
				assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
				assertNotNull(responseEntity.getBody());
				assertEquals("Germany", responseEntity.getBody().getName());
		}

		@Test
		void testUpdateCountry() {
				// Prepare data
				long countryId = 1L;
				Country updatedCountry = new Country();
				updatedCountry.setId(countryId);
				updatedCountry.setName("Updated USA");

				// Mock service behavior
				when(countryService.updateCountry(any(Country.class), eq(countryId))).thenReturn(updatedCountry);

				// Call controller method
				ResponseEntity<Country> responseEntity = countryController.updateCountry(countryId, updatedCountry);

				// Assertions
				assertNotNull(responseEntity);
				assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
				assertEquals(updatedCountry, responseEntity.getBody());
		}

		@Test
		void testUpdateCountryNotFound() {
				// Prepare data
				long nonExistingId = 999L;
				Country updatedCountry = new Country();
				updatedCountry.setId(nonExistingId);
				updatedCountry.setName("Updated Country");

				// Mock service behavior to return Optional.empty() indicating not found
				when(countryService.updateCountry(any(Country.class), eq(nonExistingId))).thenReturn(null); // Assuming updateCountry returns null if not found

				// Call controller method
				ResponseEntity<Country> responseEntity = countryController.updateCountry(nonExistingId, updatedCountry);

				// Assertions
				assertNotNull(responseEntity);
				assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
				assertNull(responseEntity.getBody());
		}

		@Test
		void testDeleteCountry() {
				// Prepare data
				long countryId = 1L;

				// Mock service behavior
				when(countryService.deleteCountry(countryId)).thenReturn(true);

				// Call controller method
				ResponseEntity<Boolean> responseEntity = countryController.deleteCountry(countryId);

				// Assertions
				assertNotNull(responseEntity);
				assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
				assertTrue(responseEntity.getBody());
		}

		@Test
		void testUpdateCountryValidation() {
				// Prepare invalid updated country (empty country object)
				Country invalidUpdatedCountry = new Country();

				// Call controller method with invalid updated country
				ResponseEntity<Country> responseEntity = countryController.updateCountry(1L, invalidUpdatedCountry);

				// Assertions
				assertNotNull(responseEntity);
				assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
				assertNull(responseEntity.getBody());
				// You can further validate the response body for specific validation error messages
		}

		@Test
		void testDeleteNonExistingCountry() {
				// Prepare non-existing country ID (negative value)
				long nonExistingId = -1L; // assuming nonExistingId is negative

				// Call controller method
				ResponseEntity<Boolean> responseEntity = countryController.deleteCountry(nonExistingId);

				// Assertions
				assertNotNull(responseEntity);

				// Check if status code is NOT_FOUND
				assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

				// Check if response body is false (indicating deletion failure)
				assertFalse(responseEntity.getBody());

				// Verify that countryService.deleteCountry() was not called
				verify(countryService, never()).deleteCountry(anyLong());
		}

		@Test
		void testGetAllCountriesEmptyList() {
				// Prepare mock data with empty list
				List<Country> mockCountries = new ArrayList<>();

				// Mock service method
				when(countryService.getAllCountries()).thenReturn(mockCountries);

				// Call controller method
				ResponseEntity<List<Country>> responseEntity = countryController.getAllCountries();

				// Assertions
				assertNotNull(responseEntity);
				assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
				assertNotNull(responseEntity.getBody());
				assertTrue(responseEntity.getBody().isEmpty());
		}

		@Test
		void testGetCountryByIdWithInvalidId() {
				// Prepare invalid country ID (negative ID)
				long invalidId = -1L;

				// Call controller method with invalid ID
				ResponseEntity<Country> responseEntity = countryController.getCountryById(invalidId);

				// Assertions
				assertNotNull(responseEntity);
				assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode()); // Assuming bad request for invalid ID
				assertNull(responseEntity.getBody());
		}

}
