package com.search.wiki.service.utils;

import com.search.wiki.controller.dto.CountryDto;
import com.search.wiki.controller.dto.UserDto;
import com.search.wiki.entity.Country;
import com.search.wiki.entity.User;
import java.util.ArrayList;
import java.util.List;

/** The type Convert to dto. */
public class ConvertToDto {
  private ConvertToDto() {}

  /**
   * Convert country to dto country dto.
   *
   * @param country the country
   * @return the country dto
   */
  public static CountryDto convertCountryToDto(Country country) {
    if (country == null) {
      return null;
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
   */
  public static UserDto convertUserToDto(User user) {
    if (user == null) {
      return null;
    }

    UserDto userDto = new UserDto();
    userDto.setId(user.getId());
    userDto.setUsername(user.getUsername());
    userDto.setEmail(user.getEmail());
    userDto.setPassword(user.getPassword());
    userDto.setCountry(convertCountryToDto(user.getCountry()));
    return userDto;
  }

  /**
   * Convert to user user.
   *
   * @param userDto the user dto
   * @return the user
   */
  public static User convertToUser(UserDto userDto) {
    User user = new User();
    user.setUsername(userDto.getUsername());
    user.setEmail(userDto.getEmail());
    user.setPassword(userDto.getPassword());
    return user;
  }

  /**
   * Convert to user dto user dto.
   *
   * @param user the user
   * @return the user dto
   */
  public static UserDto convertToUserDto(User user) {
    UserDto userDto = new UserDto();
    userDto.setId(user.getId());
    userDto.setUsername(user.getUsername());
    userDto.setEmail(user.getEmail());
    userDto.setPassword(user.getPassword());
    userDto.setCountry(convertCountryToDto(user.getCountry()));
    return userDto;
  }

  /**
   * Convert user list to dto list.
   *
   * @param userList the user list
   * @return the list
   */
  public static List<UserDto> convertUserListToDto(List<User> userList) {
    List<UserDto> userDtoList = new ArrayList<>();
    for (User user : userList) {
      userDtoList.add(convertToUserDto(user));
    }
    return userDtoList;
  }
}
