package com.search.wiki.service;

import com.search.wiki.cache.Cache;
import com.search.wiki.controller.dto.CountryDTO;
import com.search.wiki.controller.dto.UserDTO;
import com.search.wiki.entity.Country;
import com.search.wiki.entity.User;
import com.search.wiki.repository.CountryRepository;
import com.search.wiki.repository.UserRepository;
import com.search.wiki.service.utils.ConvertToDTO;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public UserDTO addUserAndCountry(UserDTO userDTO, String countryName) {
        User user = ConvertToDTO.convertToUser(userDTO);
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
        return ConvertToDTO.convertToUserDTO(addedUser);
    }

    public List<UserDTO> getAllUsersInCountry(Long countryId) {
        Country country = getCountryFromCache(countryId);
        if (country == null) {
            country = countryService.getCountryById(countryId);
            if (country != null) {
                String cacheKey = getCountryCacheKey(country.getId());
                countryCache.put(cacheKey, country);
            } else {
                return Collections.emptyList();
            }
        }

        final Country finalCountry = country;
        return userService.getAllUsers()
                .stream()
                .filter(user -> finalCountry.equals(user.getCountry()))
                .map(this::convertToDTO)
                .toList();
    }

    public UserDTO addCountryToUser(Long userId, Long countryId) {
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

            if (updatedUser != null) {
                userCache.put(userCacheKey, updatedUser);
                return convertToDTO(updatedUser);
            }
        }

        return null;
    }


    public void removeCountryFromUser(Long userId) {
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
            }
        }
    }

    public UserDTO updateUserCountry(Long userId, Long countryId) {
        String userCacheKey = getUserCacheKey(userId);
        User user = (User) userCache.get(userCacheKey);
        String countryCacheKey = getCountryCacheKey(countryId);
        Country country = (Country) countryCache.get(countryCacheKey);

        if (user == null) {
            user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                userCache.put(userCacheKey, user);
            }
        }

        if (country == null) {
            country = countryRepository.findById(countryId).orElse(null);
            if (country != null) {
                countryCache.put(countryCacheKey, country);
            }
        }

        if (user != null && country != null) {
            user.setCountry(country);
            User updatedUser = userRepository.save(user);

            if (updatedUser != null) {
                userCache.put(userCacheKey, updatedUser);
                return convertToDTO(updatedUser);
            }
        }
        return null;
    }

    public List<UserDTO> getAllUsersWithCountries() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOs = new ArrayList<>();

        for (User user : users) {
            UserDTO userDTO = convertToDTO(user);

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

                userDTO.setCountry(ConvertToDTO.convertCountryToDTO(country));
            }

            userDTOs.add(userDTO);
        }

        return userDTOs;
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        Hibernate.initialize(user.getFavoriteArticles());
        Country country = user.getCountry();

        if (country != null) {
            CountryDTO countryDTO = new CountryDTO();
            countryDTO.setId(country.getId());
            countryDTO.setName(country.getName());
            userDTO.setCountry(countryDTO);
        }

        return userDTO;
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

