package com.search.wiki.dto_tests;

import static org.junit.jupiter.api.Assertions.*;

import com.search.wiki.controller.dto.CountryDto;
import org.junit.jupiter.api.Test;

class CountryDtoTest {

  @Test
  void testCountryDtoConstructorAndGetters() {
    Long expectedId = 1L;
    String expectedName = "CountryName";

    CountryDto countryDto = new CountryDto(expectedId, expectedName);

    assertEquals(expectedId, countryDto.getId());
    assertEquals(expectedName, countryDto.getName());
  }

  @Test
  void testCountryDtoSetters() {
    CountryDto countryDto = new CountryDto();
    Long expectedId = 2L;
    String expectedName = "UpdatedCountryName";

    countryDto.setId(expectedId);
    countryDto.setName(expectedName);

    assertEquals(expectedId, countryDto.getId());
    assertEquals(expectedName, countryDto.getName());
  }

  @Test
  void testEqualsAndHashCode() {

    CountryDto countryDto1 = new CountryDto(1L, "CountryName");
    CountryDto countryDto2 = new CountryDto(1L, "CountryName");

    assertEquals(countryDto1, countryDto2);
    assertEquals(countryDto1.hashCode(), countryDto2.hashCode());
  }

  @Test
  void testNotEquals() {
    CountryDto countryDto1 = new CountryDto(1L, "CountryName1");
    CountryDto countryDto2 = new CountryDto(2L, "CountryName2");

    assertNotEquals(countryDto1, countryDto2);
  }

  @Test
  void testEqualsWithNull() {
    CountryDto countryDto = new CountryDto(1L, "CountryName");

    assertNotEquals(null, countryDto);
  }

  @Test
  void testEqualsWithDifferentClass() {
    CountryDto countryDto = new CountryDto(1L, "CountryName");
    assertNotEquals(countryDto, new Object());
  }
}
