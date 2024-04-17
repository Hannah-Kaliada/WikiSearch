package com.search.wiki.service.utils;

import com.search.wiki.controller.dto.CountryDto;
import com.search.wiki.controller.dto.UserDto;
import com.search.wiki.entity.Country;
import com.search.wiki.entity.User;
import com.search.wiki.exceptions.customexceptions.InvalidDataException;

/** The type Convert to dto. */
public class ConvertToDto {
  private ConvertToDto() {}

  /**
   * Convert country to dto country dto.
   *
   * @param country the country
   * @return the country dto
   * @throws InvalidDataException the invalid data exception
   */
  public static CountryDto convertCountryToDto(Country country) throws InvalidDataException {
    if (country == null) {
      throw new InvalidDataException("Country is null. Cannot convert to CountryDto.");
    }

    CountryDto countryDto = new CountryDto();
    countryDto.setName(country.getName());
    countryDto.setId(country.getId());
    return countryDto;
  }

  /**
   * Convert user to dto user dto.
   *
   * @param user the user
   * @return the user dto
   * @throws InvalidDataException the invalid data exception
   */
  public static UserDto convertUserToDto(User user) throws InvalidDataException {
    if (user == null) {
      throw new InvalidDataException("User is null. Cannot convert to UserDto.");
    }

    UserDto userDto = new UserDto();
    userDto.setId(user.getId());
    userDto.setUsername(user.getUsername());
    userDto.setEmail(user.getEmail());
    userDto.setPassword(user.getPassword());
    userDto.setCountry(convertCountryToDto(user.getCountry())); // May throw InvalidDataException
    return userDto;
  }

  /**
   * Convert to user user.
   *
   * @param userDto the user dto
   * @return the user
   * @throws InvalidDataException the invalid data exception
   */
  public static User convertToUser(UserDto userDto) throws InvalidDataException {
    if (userDto == null) {
      throw new InvalidDataException("UserDto is null. Cannot convert to User.");
    }

    User user = new User();
    user.setUsername(userDto.getUsername());
    user.setEmail(userDto.getEmail());
    user.setPassword(userDto.getPassword());
    return user;
  }

}
