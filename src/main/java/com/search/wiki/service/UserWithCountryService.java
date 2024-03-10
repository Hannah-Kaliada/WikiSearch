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
            List<UserDTO> usersInCountry = userService.getAllUsers()
                    .stream()
                    .filter(user -> country.equals(user.getCountry()))
                    .map(this::convertToDTO)
                    .toList();
            return usersInCountry;
        }
        return null;
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

        return null;
    }

    @Transactional
    public void removeCountryFromUser(Long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            user.setCountry(null);
            userService.updateUser(user);
        }
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

        return null;
    }

    @Transactional
    public List<UserDTO> getAllUsersWithCountries() {
        List<User> users = userService.getAllUsers();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
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
