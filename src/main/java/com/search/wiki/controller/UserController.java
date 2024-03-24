package com.search.wiki.controller;

import com.search.wiki.controller.dto.UserDTO;
import com.search.wiki.entity.User;
import com.search.wiki.service.CountryService;
import com.search.wiki.service.UserService;
import com.search.wiki.service.UserWithCountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserWithCountryService userWithCountryService;
    private final CountryService countryService;

    @Autowired
    public UserController(UserService userService, UserWithCountryService userWithCountryService, CountryService countryService) {
        this.userService = userService;
        this.userWithCountryService = userWithCountryService;
        this.countryService = countryService;
    }

    @PostMapping("/addUserAndCountry/{countryName}")
    public ResponseEntity<UserDTO> addUserAndCountry(@RequestBody UserDTO userDTO, @PathVariable String countryName) {
        UserDTO addedUser = userWithCountryService.addUserAndCountry(userDTO, countryName);
        if (addedUser != null) {
            return ResponseEntity.ok(addedUser);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/addUser")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("/updateUser/{userId}")
    public User updateUser(@RequestBody User user, @PathVariable("userId") long userId) {
        return userService.updateUser(user, userId);
    }

    @DeleteMapping("/deleteUser/{id}")
    public boolean deleteUser(@PathVariable long id) {
        return userService.deleteUser(id);
    }


    @GetMapping("/getAllUsersInCountry/{countryId}")
    public ResponseEntity<List<UserDTO>> getAllUsersInCountry(@PathVariable Long countryId) {
        List<UserDTO> users = userWithCountryService.getAllUsersInCountry(countryId);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/addCountryToUser/{userId}/{countryId}")
    public ResponseEntity<UserDTO> addCountryToUser(
            @PathVariable Long userId,
            @PathVariable Long countryId
    ) {
        UserDTO updatedUser = userWithCountryService.addCountryToUser(userId, countryId);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/removeCountryFromUser/{userId}")
    public ResponseEntity<Void> removeCountryFromUser(@PathVariable Long userId) {
        userWithCountryService.removeCountryFromUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updateUserCountry/{userId}/{countryId}")
    public ResponseEntity<UserDTO> updateUserCountry(
            @PathVariable Long userId,
            @PathVariable Long countryId
    ) {
        UserDTO updatedUser = userWithCountryService.updateUserCountry(userId, countryId);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/getAllUsersWithCountries")
    public ResponseEntity<List<UserDTO>> getAllUsersWithCountries() {
        List<UserDTO> users = userWithCountryService.getAllUsersWithCountries();
        return ResponseEntity.ok(users);
    }
}