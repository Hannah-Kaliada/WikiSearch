package com.search.wiki.service;

import com.search.wiki.controller.dto.CountryDTO;
import com.search.wiki.controller.dto.UserDTO;
import com.search.wiki.entity.Country;
import com.search.wiki.entity.User;

public class ConvertToDTO {

    public static CountryDTO convertCountryToDTO(Country country) {
        if (country == null) {
            return null;
        }

        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setName(country.getName());
        countryDTO.setId(country.getId());
        return countryDTO;
    }

    public static UserDTO convertUserToDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setCountry(convertCountryToDTO(user.getCountry()));
        return userDTO;
    }
}
