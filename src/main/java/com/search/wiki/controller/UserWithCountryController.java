package com.search.wiki.controller;

import com.search.wiki.controller.dto.UserDTO;
import com.search.wiki.service.UserWithCountryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/userCountry")
@AllArgsConstructor
public class UserWithCountryController {

    private final UserWithCountryService userCountryService;

    @GetMapping("/getAllUsersInCountry/{countryId}")
    public ResponseEntity<List<UserDTO>> getAllUsersInCountry(@PathVariable Long countryId) {
        List<UserDTO> users = userCountryService.getAllUsersInCountry(countryId);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/addCountryToUser/{userId}/{countryId}")
    public ResponseEntity<UserDTO> addCountryToUser(
            @PathVariable Long userId,
            @PathVariable Long countryId
    ) {
        UserDTO updatedUser = userCountryService.addCountryToUser(userId, countryId);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/removeCountryFromUser/{userId}")
    public ResponseEntity<Void> removeCountryFromUser(@PathVariable Long userId) {
        userCountryService.removeCountryFromUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updateUserCountry/{userId}/{countryId}")
    public ResponseEntity<UserDTO> updateUserCountry(
            @PathVariable Long userId,
            @PathVariable Long countryId
    ) {
        UserDTO updatedUser = userCountryService.updateUserCountry(userId, countryId);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/getAllUsersWithCountries")
    public ResponseEntity<List<UserDTO>> getAllUsersWithCountries() {
        List<UserDTO> users = userCountryService.getAllUsersWithCountries();
        return ResponseEntity.ok(users);
    }
}
