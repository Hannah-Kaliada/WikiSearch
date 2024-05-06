package com.search.wiki.controller;

import com.search.wiki.controller.dto.CountryDto;
import com.search.wiki.controller.dto.UserDto;
import com.search.wiki.entity.User;
import com.search.wiki.service.CountryService;
import com.search.wiki.service.UserService;
import com.search.wiki.service.UserWithCountryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** The type User controller. */
@RestController
@CrossOrigin(origins = "*")
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
@CrossOrigin
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
@CrossOrigin
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
@CrossOrigin
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
@CrossOrigin
  public ResponseEntity<User> addUser(@RequestBody User user) {
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
@PutMapping("/updateUser/{userId}")
@CrossOrigin
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
@DeleteMapping("/deleteUser/{id}")
@CrossOrigin
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
@CrossOrigin
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
@CrossOrigin
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
@CrossOrigin
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
@CrossOrigin
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
@CrossOrigin
  public ResponseEntity<List<UserDto>> getAllUsersWithCountries() {
    List<UserDto> users = userWithCountryService.getAllUsersWithCountries();
    return ResponseEntity.ok(users);
  }

  /**
   * Gets count.
   *
   * @return the count
   */
  @GetMapping("/count")
  @CrossOrigin
  public int getCount() {
    return userService.getRequestCount();
  }
  @GetMapping("/{userId}/country")
  @CrossOrigin
  public ResponseEntity<CountryDto> getCountryByUserId(@PathVariable long userId) {
    CountryDto countryDto = userWithCountryService.getCountryByUserId(userId);

    if (countryDto != null) {
      return ResponseEntity.ok(countryDto);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/login")
  @CrossOrigin
  public long loginUser(@RequestBody User loginRequest) {
    return userService.getUserIdByEmailAndPassword(
        loginRequest.getEmail(), loginRequest.getPassword());
  }
}
