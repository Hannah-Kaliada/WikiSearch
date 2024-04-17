package com.search.wiki.service_tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.search.wiki.cache.Cache;
import com.search.wiki.controller.dto.CountryDto;
import com.search.wiki.controller.dto.UserDto;
import com.search.wiki.entity.Country;
import com.search.wiki.entity.User;
import com.search.wiki.repository.CountryRepository;
import com.search.wiki.repository.UserRepository;
import com.search.wiki.service.CountryService;
import com.search.wiki.service.UserService;
import com.search.wiki.service.UserWithCountryService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/** The type User with country service test. */
class UserWithCountryServiceTest {

  @Mock private UserService userService;

  /** The Country service. */
  @Mock CountryService countryService;

  /** The User cache. */
  @Mock Cache userCache;

  /** The Country cache. */
  @Mock Cache countryCache;

  /** The User repository. */
  @Mock UserRepository userRepository;

  /** The Country repository. */
  @Mock CountryRepository countryRepository;

  /** The User with country service. */
  @InjectMocks UserWithCountryService userWithCountryService;

  /** Sets up. */
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  /** Test add user and country success. */
  @Test
  void testAddUserAndCountry_Success() {
    String countryName = "USA";

    when(countryService.getCountryIdByName(countryName)).thenReturn(1L);

    Country country = new Country(1L, countryName);
    when(countryService.getCountryById(1L)).thenReturn(country);

    UserDto userDto = new UserDto();
    userDto.setUsername("john_doe");
    userDto.setEmail("john@example.com");
    userDto.setPassword("password");
    userDto.setCountry(new CountryDto(1L, countryName));

    User userToAdd = new User("john_doe", "john@example.com", "password", country);
    when(userService.addUser(any(User.class))).thenReturn(userToAdd);

    UserDto result = userWithCountryService.addUserAndCountry(userDto, countryName);

    assertNotNull(result);
    assertEquals(userDto.getUsername(), result.getUsername());
    assertEquals(userDto.getEmail(), result.getEmail());
    assertEquals(userDto.getPassword(), result.getPassword());
    assertNotNull(result.getCountry());
    assertEquals(1L, result.getCountry().getId());
    assertEquals(countryName, result.getCountry().getName());

    verify(countryService, times(1)).getCountryIdByName(countryName);
    verify(countryService, times(1)).getCountryById(1L);
    verify(userService, times(1)).addUser(any(User.class));
  }

  /** Test add user and country null country name throws exception. */
  @Test
  void testAddUserAndCountry_NullCountryName_ThrowsException() {
    // Arrange
    UserDto userDto = new UserDto();
    userDto.setUsername("john_doe");
    userDto.setEmail("john@example.com");
    userDto.setPassword("password");
    userDto.setCountry(null);
    assertThrows(
        IllegalArgumentException.class,
        () -> userWithCountryService.addUserAndCountry(userDto, null));
  }

  /** Test add country to user invalid user id throws exception. */
  @Test
  void testAddCountryToUser_InvalidUserId_ThrowsException() {
    Long invalidUserId = -1L;
    Long countryId = 2L;

    assertThrows(
        IllegalArgumentException.class,
        () -> userWithCountryService.addCountryToUser(invalidUserId, countryId));
  }

  /** Test remove country from user success. */
  @Test
  void testRemoveCountryFromUser_Success() {
    Long userId = 1L;

    User existingUser = new User();
    existingUser.setId(userId);
    existingUser.setCountry(new Country());

    when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
    when(userCache.get("User_" + userId)).thenReturn(existingUser);
    doNothing().when(userCache).put(eq("User_1"), isNull());
    userWithCountryService.removeCountryFromUser(userId);

    assertNull(existingUser.getCountry());
    verify(userRepository, times(1)).save(existingUser);
  }

  /** Test update user country invalid country id throws exception. */
  @Test
  void testUpdateUserCountry_InvalidCountryId_ThrowsException() {

    Long userId = 1L;
    Long invalidCountryId = -1L;

    assertThrows(
        IllegalArgumentException.class,
        () -> userWithCountryService.updateUserCountry(userId, invalidCountryId));
  }

  /** Test get all users with countries success. */
  @Test
  void testGetAllUsersWithCountries_Success() {
    User user1 = new User(1L, "john_doe", "john@example.com", "password", new Country(1L, "USA"));
    User user2 = new User(2L, "jane_doe", "jane@example.com", "password", new Country(1L, "USA"));

    List<User> users = new ArrayList<>();
    users.add(user1);
    users.add(user2);
    UserService userService = mock(UserService.class);
    when(userService.getAllUsers()).thenReturn(users);

    CountryService countryService = mock(CountryService.class);
    Country country = new Country(1L, "USA");
    when(countryService.getCountryById(1L)).thenReturn(country);

    UserWithCountryService userWithCountryService =
        new UserWithCountryService(
            userService,
            countryService,
            userCache,
            countryCache,
            userRepository,
            countryRepository);

    List<UserDto> result = userWithCountryService.getAllUsersWithCountries();

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(user1.getUsername(), result.get(0).getUsername());
    assertEquals(user2.getUsername(), result.get(1).getUsername());
    assertEquals(country.getName(), result.get(0).getCountry().getName());
    assertEquals(country.getName(), result.get(1).getCountry().getName());

    verify(userService, times(1)).getAllUsers();
    verify(countryService, times(2)).getCountryById(1L);
  }
}
