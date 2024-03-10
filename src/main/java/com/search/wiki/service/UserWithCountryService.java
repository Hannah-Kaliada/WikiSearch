package com.search.wiki.service;

import com.search.wiki.controller.dto.UserDTO;
import java.util.List;

public interface UserWithCountryService {
    List<UserDTO> getAllUsersInCountry(Long countryId);

    UserDTO addCountryToUser(Long userId, Long countryId);

    void removeCountryFromUser(Long userId);

    UserDTO updateUserCountry(Long userId, Long countryId);

    List<UserDTO> getAllUsersWithCountries();
}
