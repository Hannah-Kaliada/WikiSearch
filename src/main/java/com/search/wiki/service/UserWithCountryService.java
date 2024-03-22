package com.search.wiki.service;

import com.search.wiki.controller.dto.CountryDTO;
import com.search.wiki.controller.dto.UserDTO;
import com.search.wiki.entity.Country;
import com.search.wiki.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class UserWithCountryService {

    private final UserService userService;
    private final CountryService countryService;

    public List<UserDTO> getAllUsersInCountry(Long countryId) {
        Country country = countryService.getCountryById(countryId);
        if (country != null) {
            return userService.getAllUsers()
                    .stream()
                    .filter(user -> country.equals(user.getCountry()))
                    .map(this::convertToDTO)
                    .toList();
        }
        return Collections.emptyList();
    }

    public UserDTO addCountryToUser(Long userId, Long countryId) {
        User user = userService.getUserById(userId);
        Country country = countryService.getCountryById(countryId);

        if (user != null && country != null) {
            user.setCountry(country);
            userService.updateUser(user);
            return convertToDTO(user);
        }

        return null;
    }

    public void removeCountryFromUser(Long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            user.setCountry(null);
            userService.updateUser(user);
        }
    }

    public UserDTO updateUserCountry(Long userId, Long countryId) {
        User user = userService.getUserById(userId);
        Country country = countryService.getCountryById(countryId);

        if (user != null && country != null) {
            if (user.getCountry() != null) {
                user.getCountry().setName(country.getName());
                userService.updateUser(user);
            } else {
                user.setCountry(country);
                userService.updateUser(user);
            }
            return convertToDTO(user);
        }
        return null;
    }

    public List<UserDTO> getAllUsersWithCountries() {
        List<User> users = userService.getAllUsers();
        return users.stream()
                .map(this::convertToDTO)
                .toList();
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());

        Country country = user.getCountry();
        if (country != null) {
            CountryDTO countryDTO = new CountryDTO();
            countryDTO.setId(country.getId());
            countryDTO.setName(country.getName());
            userDTO.setCountry(countryDTO);
        }

        return userDTO;
    }
}
