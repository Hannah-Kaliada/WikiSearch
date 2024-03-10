package com.search.wiki.service;

import com.search.wiki.controller.dto.CountryDTO;
import com.search.wiki.controller.dto.UserDTO;
import com.search.wiki.entity.Country;
import com.search.wiki.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserWithCountryService {

    private final UserService userService;
    private final CountryService countryService;

    @Transactional
    public List<UserDTO> getAllUsersInCountry(Long countryId) {
        Country country = countryService.getCountryById(countryId);
        if (country != null) {
            List<User> usersInCountry = userService.getAllUsers()
                    .stream()
                    .filter(user -> country.equals(user.getCountry()))
                    .collect(Collectors.toList());
            return usersInCountry.stream().map(this::convertToDTO).collect(Collectors.toList());
        }
        return null; // Handle the case when the country is not found
    }

    @Transactional
    public UserDTO addCountryToUser(Long userId, Long countryId) {
        User user = userService.getUserById(userId);
        Country country = countryService.getCountryById(countryId);

        if (user != null && country != null) {
            user.setCountry(country);
            userService.updateUser(user);
            return convertToDTO(user);
        }

        return null; // Handle the case when the user or country is not found
    }

    @Transactional
    public void removeCountryFromUser(Long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            user.setCountry(null);
            userService.updateUser(user);
        }
        // Handle the case when the user is not found
    }

    @Transactional
    public UserDTO updateUserCountry(Long userId, Long countryId) {
        User user = userService.getUserById(userId);
        Country country = countryService.getCountryById(countryId);

        if (user != null && country != null) {
            user.setCountry(country);
            userService.updateUser(user);
            return convertToDTO(user);
        }

        return null; // Handle the case when the user or country is not found
    }

    @Transactional
    public List<UserDTO> getAllUsersWithCountries() {
        List<User> users = userService.getAllUsers();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        // Convert User entity to UserDTO
        UserDTO userDTO = new UserDTO();
        // Map user properties to userDTO properties
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());

        // Map country information
        Country country = user.getCountry();
        if (country != null) {
            CountryDTO countryDTO = new CountryDTO();
            countryDTO.setId(country.getId());
            countryDTO.setName(country.getName());
            // ... (other properties)
            userDTO.setCountry(countryDTO);
        }

        return userDTO;
    }
}
