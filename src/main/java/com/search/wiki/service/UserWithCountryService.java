package com.search.wiki.service;

import com.search.wiki.cache.Cache;
import com.search.wiki.controller.dto.CountryDto;
import com.search.wiki.controller.dto.UserDto;
import com.search.wiki.entity.Country;
import com.search.wiki.entity.User;
import com.search.wiki.exceptions.ExceptionConstants;
import com.search.wiki.exceptions.customexceptions.NotFoundException;
import com.search.wiki.repository.CountryRepository;
import com.search.wiki.repository.UserRepository;
import com.search.wiki.service.utils.ConvertToDto;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** The type User with country service. */
@Service
@AllArgsConstructor
@Transactional
public class UserWithCountryService {

  private final UserService userService;
  private final CountryService countryService;
  private final Cache userCache;
  private final Cache countryCache;
  private final UserRepository userRepository;
  private final CountryRepository countryRepository;

  /**
   * Add user and country user dto.
   *
   * @param userDto the user dto
   * @param countryName the country name
   * @return the user dto
   */
  public UserDto addUserAndCountry(UserDto userDto, String countryName) {
    if (userDto == null || countryName == null) {
      throw new IllegalArgumentException("User and country name cannot be null");
    }
    User user = ConvertToDto.convertToUser(userDto);
    Long countryId = countryService.getCountryIdByName(countryName);

    if (countryId != null) {
      Country country = getCountryFromCache(countryId);

      if (country == null) {
        country = countryService.getCountryById(countryId);
        String countryCacheKey = getCountryCacheKey(countryId);
        countryCache.put(countryCacheKey, country);
      }

      user.setCountry(country);
    } else {
      Country newCountry = new Country();
      newCountry.setName(countryName);
      countryId = countryService.addCountryAndGetId(newCountry);
      String countryCacheKey = getCountryCacheKey(countryId);
      countryCache.put(countryCacheKey, newCountry);
      user.setCountry(newCountry);
    }
    User addedUser = userService.addUser(user);
    return ConvertToDto.convertUserToDto(addedUser);
  }

  /**
   * Gets all users in country.
   *
   * @param countryId the country id
   * @return the all users in country
   */
  public List<UserDto> getAllUsersInCountry(Long countryId) {
    if (countryId < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
    }
    Country country = getCountryFromCache(countryId);
    if (country == null) {
      country = countryService.getCountryById(countryId);
      if (country != null) {
        String cacheKey = getCountryCacheKey(country.getId());
        countryCache.put(cacheKey, country);
      } else {
        throw new NotFoundException(ExceptionConstants.COUNTRY_NOT_FOUND + countryId);
      }
    }

    final Country finalCountry = country;
    return userService.getAllUsers().stream()
        .filter(user -> finalCountry.equals(user.getCountry()))
        .map(this::convertToDto)
        .toList();
  }

  /**
   * Add country to user dto.
   *
   * @param userId the user id
   * @param countryId the country id
   * @return the user dto
   */
  public UserDto addCountryToUser(Long userId, Long countryId) {
    if (userId < 1 || countryId < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
    }

    String userCacheKey = getUserCacheKey(userId);
    User user = (User) userCache.get(userCacheKey);

    if (user == null) {
      user =
          userRepository
              .findById(userId)
              .orElseThrow(() -> new NotFoundException(ExceptionConstants.USER_NOT_FOUND + userId));
    }

    String countryCacheKey = getCountryCacheKey(countryId);
    Country country = (Country) countryCache.get(countryCacheKey);

    if (country == null) {
      country =
          countryRepository
              .findById(countryId)
              .orElseThrow(
                  () -> new NotFoundException(ExceptionConstants.COUNTRY_NOT_FOUND + countryId));
      countryCache.put(countryCacheKey, country);
    }

    user.setCountry(country);
    User updatedUser = userRepository.save(user);
    userCache.put(userCacheKey, updatedUser);

    return convertToDto(updatedUser);
  }

  /**
   * Remove country from user.
   *
   * @param userId the user id
   */
  public void removeCountryFromUser(Long userId) {
    if (userId < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
    }

    String userCacheKey = getUserCacheKey(userId);
    User user = (User) userCache.get(userCacheKey);

    if (user == null) {
      user =
          userRepository
              .findById(userId)
              .orElseThrow(() -> new NotFoundException(ExceptionConstants.USER_NOT_FOUND + userId));
    }

    user.setCountry(null);
    User updatedUser = userRepository.save(user);

    userCache.put(userCacheKey, updatedUser);
  }

  /**
   * Update user country user dto.
   *
   * @param userId the user id
   * @param countryId the country id
   * @return the user dto
   */
  public UserDto updateUserCountry(Long userId, Long countryId) {
    if (userId < 1 || countryId < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
    }

    String userCacheKey = getUserCacheKey(userId);
    User user = (User) userCache.get(userCacheKey);

    if (user == null) {
      user =
          userRepository
              .findById(userId)
              .orElseThrow(() -> new NotFoundException(ExceptionConstants.USER_NOT_FOUND + userId));
      userCache.put(userCacheKey, user);
    }

    String countryCacheKey = getCountryCacheKey(countryId);
    Country country = (Country) countryCache.get(countryCacheKey);

    if (country == null) {
      country =
          countryRepository
              .findById(countryId)
              .orElseThrow(
                  () -> new NotFoundException(ExceptionConstants.COUNTRY_NOT_FOUND + countryId));
      countryCache.put(countryCacheKey, country);
    }

    user.setCountry(country);
    User updatedUser = userRepository.save(user);

    userCache.put(userCacheKey, updatedUser);
    return convertToDto(updatedUser);
  }

  /**
   * Gets all users with countries.
   *
   * @return the all users with countries
   */
  public List<UserDto> getAllUsersWithCountries() {
    List<User> users = userService.getAllUsers();
    List<UserDto> userDtos = new ArrayList<>();

    for (User user : users) {
      UserDto userDto = convertToDto(user);

      if (user.getCountry() != null) {
        Country country = getCountryFromCache(user.getCountry().getId());

        if (country == null) {
          country = countryService.getCountryById(user.getCountry().getId());

          if (country != null) {
            String cacheKey = getCountryCacheKey(country.getId());
            if (!countryCache.containsKey(cacheKey)) {
              countryCache.put(cacheKey, country);
            }
          }
        }

        userDto.setCountry(ConvertToDto.convertCountryToDto(country));
      }

      userDtos.add(userDto);
    }

    return userDtos;
  }
  public CountryDto getCountryByUserId(long userId) {
    User user = userService.getUserById(userId);

    if (user != null && user.getCountry() != null) {
      Country country = getCountryFromCache(user.getCountry().getId());

      if (country == null) {
        country = countryService.getCountryById(user.getCountry().getId());

        if (country != null) {
          String cacheKey = getCountryCacheKey(country.getId());
          if (!countryCache.containsKey(cacheKey)) {
            countryCache.put(cacheKey, country);
          }
        }
      }

      return ConvertToDto.convertCountryToDto(country);
    }

    return null;
  }


  public UserDto convertToDto(User user) {
    if (user == null) {
      throw new IllegalArgumentException("User cannot be null");
    }
    UserDto userDto = new UserDto();
    userDto.setId(user.getId());
    userDto.setUsername(user.getUsername());
    userDto.setEmail(user.getEmail());
    userDto.setPassword(user.getPassword());
    Hibernate.initialize(user.getFavoriteArticles());
    Country country = user.getCountry();

    if (country != null) {
      CountryDto countryDto = new CountryDto();
      countryDto.setId(country.getId());
      countryDto.setName(country.getName());
      userDto.setCountry(countryDto);
    }

    return userDto;
  }

  public Country getCountryFromCache(Long countryId) {
    String cacheKey = getCountryCacheKey(countryId);
    return (Country) countryCache.get(cacheKey);
  }

  public String getUserCacheKey(Long userId) {
    return "User_" + userId;
  }

  public String getCountryCacheKey(Long countryId) {
    return "Country_" + countryId;
  }
}
