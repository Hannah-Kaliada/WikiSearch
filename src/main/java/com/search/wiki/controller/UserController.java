package com.search.wiki.controller;

import com.search.wiki.controller.dto.UserDto;
import com.search.wiki.entity.User;
import com.search.wiki.service.CountryService;
import com.search.wiki.service.UserService;
import com.search.wiki.service.UserWithCountryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The type User controller. */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;
  private final UserWithCountryService userWithCountryService;

  /**
   * Instantiates a new User controller.
   *
   * @param userService the user service
   * @param userWithCountryService the user with country service
   * @param countryService the country service
   */
  @Autowired
  public UserController(
      UserService userService,
      UserWithCountryService userWithCountryService,
      CountryService countryService) {
    this.userService = userService;
    this.userWithCountryService = userWithCountryService;
  }

  /**
   * Add user and country response entity.
   *
   * @param userDto the user dto
   * @param countryName the country name
   * @return the response entity
   */
  @PostMapping("/addUserAndCountry/{countryName}")
  public ResponseEntity<UserDto> addUserAndCountry(
      @Valid @RequestBody UserDto userDto, @PathVariable String countryName) {
    UserDto addedUser = userWithCountryService.addUserAndCountry(userDto, countryName);
    if (addedUser != null) {
      return ResponseEntity.ok(addedUser);
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * Gets all users.
   *
   * @return the all users
   */
  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  /**
   * Gets user by id.
   *
   * @param id the id
   * @return the user by id
   */
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable long id) {
    User user = userService.getUserById(id);
    return ResponseEntity.ok(user);
  }

  /**
   * Add user response entity.
   *
   * @param user the user
   * @return the response entity
   */
  @PostMapping("/addUser")
  public ResponseEntity<User> addUser( @RequestBody User user) {
    User addedUser = userService.addUser(user);
    return ResponseEntity.ok(addedUser);
  }

  /**
   * Update user response entity.
   *
   * @param user the user
   * @param userId the user id
   * @return the response entity
   */
  @PutMapping("/api/v1/users/updateUser/{userId}")
  public ResponseEntity<User> updateUser(
      @Valid @RequestBody User user, @PathVariable("userId") long userId) {
    User updatedUser = userService.updateUser(user, userId);
    return ResponseEntity.ok(updatedUser);
  }

  /**
   * Delete user response entity.
   *
   * @param id the id
   * @return the response entity
   */
  @DeleteMapping("/api/v1/users/deleteUser/{id}")
  public ResponseEntity<Boolean> deleteUser(@PathVariable long id) {
    userService.deleteUser(id);
    return ResponseEntity.ok(true);
  }

  /**
   * Gets all users in country.
   *
   * @param countryId the country id
   * @return the all users in country
   */
  @GetMapping("/getAllUsersInCountry/{countryId}")
  public ResponseEntity<List<UserDto>> getAllUsersInCountry(@PathVariable Long countryId) {
    List<UserDto> users = userWithCountryService.getAllUsersInCountry(countryId);
    return ResponseEntity.ok(users);
  }

  /**
   * Add country to user response entity.
   *
   * @param userId the user id
   * @param countryId the country id
   * @return the response entity
   */
  @PostMapping("/addCountryToUser/{userId}/{countryId}")
  public ResponseEntity<UserDto> addCountryToUser(
      @PathVariable Long userId, @PathVariable Long countryId) {
    UserDto updatedUser = userWithCountryService.addCountryToUser(userId, countryId);
    return ResponseEntity.ok(updatedUser);
  }

  /**
   * Remove country from user response entity.
   *
   * @param userId the user id
   * @return the response entity
   */
  @DeleteMapping("/removeCountryFromUser/{userId}")
  public ResponseEntity<Void> removeCountryFromUser(@PathVariable Long userId) {
    userWithCountryService.removeCountryFromUser(userId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Update user country response entity.
   *
   * @param userId the user id
   * @param countryId the country id
   * @return the response entity
   */
  @PutMapping("/updateUserCountry/{userId}/{countryId}")
  public ResponseEntity<UserDto> updateUserCountry(
      @PathVariable Long userId, @PathVariable Long countryId) {
    UserDto updatedUser = userWithCountryService.updateUserCountry(userId, countryId);
    return ResponseEntity.ok(updatedUser);
  }

  /**
   * Gets all users with countries.
   *
   * @return the all users with countries
   */
  @GetMapping("/getAllUsersWithCountries")
  public ResponseEntity<List<UserDto>> getAllUsersWithCountries() {
    List<UserDto> users = userWithCountryService.getAllUsersWithCountries();
    return ResponseEntity.ok(users);
  }
}
