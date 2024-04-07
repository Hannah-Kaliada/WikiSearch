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
  private final CountryService countryService;

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
    this.countryService = countryService;
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
  public List<User> getAllUsers() {
    return userService.getAllUsers();
  }

  /**
   * Gets user by id.
   *
   * @param id the id
   * @return the user by id
   */
  @GetMapping("/{id}")
  public User getUserById(@PathVariable long id) {
    return userService.getUserById(id);
  }

  /**
   * Add user user.
   *
   * @param user the user
   * @return the user
   */
  @PostMapping("/addUser")
  public User addUser(@Valid @RequestBody User user) {
    return userService.addUser(user);
  }

  /**
   * Update user user.
   *
   * @param user the user
   * @param userId the user id
   * @return the user
   */
  @PutMapping("/updateUser/{userId}")
  public User updateUser(@Valid @RequestBody User user, @PathVariable("userId") long userId) {
    return userService.updateUser(user, userId);
  }

  /**
   * Delete user boolean.
   *
   * @param id the id
   * @return the boolean
   */
  @DeleteMapping("/deleteUser/{id}")
  public boolean deleteUser(@PathVariable long id) {
    return userService.deleteUser(id);
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
