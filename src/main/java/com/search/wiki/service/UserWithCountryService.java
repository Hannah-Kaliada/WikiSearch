package com.search.wiki.service;

import com.search.wiki.cache.Cache;
import com.search.wiki.controller.dto.CountryDto;
import com.search.wiki.controller.dto.UserDto;
import com.search.wiki.entity.Country;
import com.search.wiki.entity.User;
import com.search.wiki.exceptions.customexceptions.DatabaseAccessException;
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
    if (addedUser.getCountry() == null) {
      throw new DatabaseAccessException("Failed to add country to user");
    }
    if (addedUser == null) {
      throw new DatabaseAccessException("Failed to add user to the database");
    }
    return ConvertToDto.convertToUserDto(addedUser);
  }

  /**
   * Gets all users in country.
   *
   * @param countryId the country id
   * @return the all users in country
   */
  public List<UserDto> getAllUsersInCountry(Long countryId) {
    if (countryId < 1) {
      throw new IllegalArgumentException("Id cannot be less than 1");
    }
    Country country = getCountryFromCache(countryId);
    if (country == null) {
      country = countryService.getCountryById(countryId);
      if (country != null) {
        String cacheKey = getCountryCacheKey(country.getId());
        countryCache.put(cacheKey, country);
      } else {
        throw new NotFoundException("Country not found with ID: " + countryId);
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
      throw new IllegalArgumentException("Id cannot be less than 1");
    }
    String userCacheKey = getUserCacheKey(userId);
    User user = (User) userCache.get(userCacheKey);
    String countryCacheKey = getCountryCacheKey(countryId);
    Country country = (Country) countryCache.get(countryCacheKey);

    if (user == null) {
      user = userRepository.findById(userId).orElse(null);
    }

    if (country == null) {
      country = countryRepository.findById(countryId).orElse(null);
    }

    if (user != null && country != null) {
      user.setCountry(country);
      User updatedUser = userRepository.save(user);
      userCache.put(userCacheKey, updatedUser);
      return convertToDto(updatedUser);
    } else if (user == null) {
      throw new NotFoundException("User not found with ID: " + userId);
    } else if (country == null) {
      throw new NotFoundException("Country not found with ID: " + countryId);
    }

    return null;
  }

  /**
   * Remove country from user.
   *
   * @param userId the user id
   */
  public void removeCountryFromUser(Long userId) {
    if (userId < 1) {
      throw new IllegalArgumentException("Id cannot be less than 1");
    }
    String userCacheKey = getUserCacheKey(userId);
    User user = (User) userCache.get(userCacheKey);

    if (user == null) {
      user = userRepository.findById(userId).orElse(null);
    }

    if (user != null) {
      user.setCountry(null);
      User updatedUser = userRepository.save(user);

      if (updatedUser != null) {
        userCache.put(userCacheKey, updatedUser);
      } else {
        throw new DatabaseAccessException("Failed to remove country from user");
      }
    } else {
      throw new NotFoundException("User not found with ID: " + userId);
    }
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
      throw new IllegalArgumentException("Id cannot be less than 1");
    }
    String userCacheKey = getUserCacheKey(userId);
    User user = (User) userCache.get(userCacheKey);
    String countryCacheKey = getCountryCacheKey(countryId);
    Country country = (Country) countryCache.get(countryCacheKey);

    if (user == null) {
      user =
          userRepository
              .findById(userId)
              .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
      if (user != null) {
        userCache.put(userCacheKey, user);
      } else {
        throw new NotFoundException("User not found with ID: " + userId);
      }
    }

    if (country == null) {
      country =
          countryRepository
              .findById(countryId)
              .orElseThrow(() -> new NotFoundException("Country not found with ID: " + userId));
      if (country != null) {
        countryCache.put(countryCacheKey, country);
      } else {
        throw new NotFoundException("Country not found with ID: " + userId);
      }
    }

    if (user != null && country != null) {
      user.setCountry(country);
      User updatedUser = userRepository.save(user);

      if (updatedUser != null) {
        userCache.put(userCacheKey, updatedUser);
        return convertToDto(updatedUser);
      } else {
        throw new DatabaseAccessException("Failed to update user country in the database");
      }
    }
    return null;
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

  private UserDto convertToDto(User user) {
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

  private Country getCountryFromCache(Long countryId) {
    String cacheKey = getCountryCacheKey(countryId);
    return (Country) countryCache.get(cacheKey);
  }

  private String getUserCacheKey(Long userId) {
    return "User_" + userId;
  }

  private String getCountryCacheKey(Long countryId) {
    return "Country_" + countryId;
  }
}
