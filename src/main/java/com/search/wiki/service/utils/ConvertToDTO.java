package com.search.wiki.service.utils;

import com.search.wiki.controller.dto.CountryDTO;
import com.search.wiki.controller.dto.UserDTO;
import com.search.wiki.entity.Country;
import com.search.wiki.entity.User;

import java.util.ArrayList;
import java.util.List;

public class ConvertToDTO {
    private ConvertToDTO() {

    }

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
    public static User convertToUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        // Здесь можно добавить другие поля, если необходимо
        return user;
    }
    public static UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setCountry(convertCountryToDTO(user.getCountry()));
        return userDTO;
    }

    public static List<UserDTO> convertUserListToDTO(List<User> userList) {
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : userList) {
            userDTOList.add(convertToUserDTO(user));
        }
        return userDTOList;
    }

}
