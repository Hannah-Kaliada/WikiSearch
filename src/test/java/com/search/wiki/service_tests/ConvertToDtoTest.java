package com.search.wiki.service_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.search.wiki.controller.dto.CountryDto;
import com.search.wiki.controller.dto.UserDto;
import com.search.wiki.entity.Country;
import com.search.wiki.entity.User;
import com.search.wiki.exceptions.customexceptions.InvalidDataException;
import com.search.wiki.service.utils.ConvertToDto;
import org.junit.jupiter.api.Test;

/** The type Convert to dto tests. */
class ConvertToDtoTest {

  /** Convert country to dto valid country returns country dto. */
  @Test
  void convertCountryToDto_ValidCountry_ReturnsCountryDto() {

    Country country = new Country();
    country.setId(1L);
    country.setName("Test Country");

    CountryDto countryDto = ConvertToDto.convertCountryToDto(country);

    assertNotNull(countryDto);
    assertEquals(country.getId(), countryDto.getId());
    assertEquals(country.getName(), countryDto.getName());
  }

  /** Convert country to dto null country throws invalid data exception. */
  @Test
  void convertCountryToDto_NullCountry_ThrowsInvalidDataException() {

    assertThrows(InvalidDataException.class, () -> ConvertToDto.convertCountryToDto(null));
  }

  /**
   * Convert user to dto valid user returns user dto.
   *
   * @throws InvalidDataException the invalid data exception
   */
  @Test
  void convertUserToDto_ValidUser_ReturnsUserDto() throws InvalidDataException {

    Country country = new Country();
    country.setId(1L);
    country.setName("Test Country");

    User user = new User();
    user.setId(1L);
    user.setUsername("testuser");
    user.setEmail("test@example.com");
    user.setPassword("password");
    user.setCountry(country);

    UserDto userDto = ConvertToDto.convertUserToDto(user);

    assertNotNull(userDto);
    assertEquals(user.getId(), userDto.getId());
    assertEquals(user.getUsername(), userDto.getUsername());
    assertEquals(user.getEmail(), userDto.getEmail());
    assertEquals(user.getPassword(), userDto.getPassword());
    assertEquals(user.getCountry().getId(), userDto.getCountry().getId());
    assertEquals(user.getCountry().getName(), userDto.getCountry().getName());
  }

  /** Convert user to dto null user throws invalid data exception. */
  @Test
  void convertUserToDto_NullUser_ThrowsInvalidDataException() {

    assertThrows(InvalidDataException.class, () -> ConvertToDto.convertUserToDto(null));
  }

  /**
   * Convert to user valid user dto returns user.
   *
   * @throws InvalidDataException the invalid data exception
   */
  @Test
  void convertToUser_ValidUserDto_ReturnsUser() throws InvalidDataException {

    UserDto userDto = new UserDto();
    userDto.setUsername("testuser");
    userDto.setEmail("test@example.com");
    userDto.setPassword("password");

    User user = ConvertToDto.convertToUser(userDto);

    assertNotNull(user);
    assertEquals(userDto.getUsername(), user.getUsername());
    assertEquals(userDto.getEmail(), user.getEmail());
    assertEquals(userDto.getPassword(), user.getPassword());
    assertNull(user.getCountry());
  }
}
