package com.search.wiki.controller;

import com.search.wiki.controller.dto.UserDTO;
import com.search.wiki.entity.Country;
import com.search.wiki.entity.User;
import com.search.wiki.service.CountryService;
import com.search.wiki.service.UserService;
import com.search.wiki.service.UserWithCountryService;
import com.search.wiki.service.utils.ConvertToDTO;
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
        // Преобразуем UserDTO в объект User
        User user = ConvertToDTO.convertToUser(userDTO);

        // Добавляем пользователя
        User addedUser = userService.addUser(user);
        if (addedUser == null) {
            return ResponseEntity.badRequest().build(); // Если не удалось добавить пользователя, возвращаем код ошибки 400
        }

        // Проверяем наличие страны по названию
        Country country = countryService.getCountryByName(countryName);
        if (country == null) {
            // Если страны не существует, создаем новую страну
            country = new Country();
            country.setName(countryName);
            countryService.addCountry(country);
        }

        // Добавляем страну к пользователю
        UserDTO userWithCountry = userWithCountryService.addCountryToUser(addedUser.getId(), country.getId());
        if (userWithCountry == null) {
            return ResponseEntity.badRequest().build(); // Если не удалось добавить страну пользователю, возвращаем код ошибки 400
        }

        return ResponseEntity.ok(userWithCountry);
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

    @PutMapping("updateUser")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("deleteUser/{id}")
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