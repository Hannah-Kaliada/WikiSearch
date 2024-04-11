package com.search.wiki.controller_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.search.wiki.controller.UserController;
import com.search.wiki.controller.dto.UserDto;
import com.search.wiki.entity.User;
import com.search.wiki.service.UserService;
import com.search.wiki.service.UserWithCountryService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

 class UserControllerTest {

  @Test
  void testGetUserById() {
    // Arrange
    long userId = 1L;
    String username = "John Doe";

    User expectedUser = new User();
    expectedUser.setId(userId);
    expectedUser.setUsername(username);

    UserService userService = mock(UserService.class);
    when(userService.getUserById(userId)).thenReturn(expectedUser);

    UserController userController = new UserController(userService, null, null);

    // Act
    ResponseEntity<User> responseEntity = userController.getUserById(userId);

    // Assert
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(expectedUser, responseEntity.getBody());

    // Verify that userService.getUserById() was called once with the correct userId
    verify(userService, times(1)).getUserById(userId);
  }

  @Test
  void testDeleteUser() {
    // Arrange
    long userId = 1L;

    UserService userService = mock(UserService.class);
    when(userService.deleteUser(userId)).thenReturn(true); // Mocking the return value

    UserController userController = new UserController(userService, null, null);

    // Act
    ResponseEntity<Boolean> responseEntity = userController.deleteUser(userId);

    // Assert
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertTrue(responseEntity.getBody());

    // Verify that userService.deleteUser() was called once with the correct userId
    verify(userService, times(1)).deleteUser(userId);
  }

  @Test
  void testAddUser() {
    // Arrange
    long userId = 1L;
    String username = "John Doe";

    User userToAdd = new User();
    userToAdd.setId(userId);
    userToAdd.setUsername(username);
    UserService userService = mock(UserService.class);
    when(userService.addUser(userToAdd)).thenReturn(userToAdd); // Mocking the added user

    UserController userController = new UserController(userService, null, null);

    // Act
    ResponseEntity<User> responseEntity = userController.addUser(userToAdd);

    // Assert
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(userToAdd, responseEntity.getBody());

    // Verify that userService.addUser() was called once with the correct user
    verify(userService, times(1)).addUser(userToAdd);
  }

  @Test
  void testUpdateUser() {
    // Arrange
    long userId = 1L;
    String username = "new John Doe";

    User updatedUser = new User();
    updatedUser.setId(userId);
    updatedUser.setUsername(username);

    UserService userService = mock(UserService.class);
    when(userService.updateUser(updatedUser, userId))
        .thenReturn(updatedUser); // Mocking the updated user

    UserController userController = new UserController(userService, null, null);

    // Act
    ResponseEntity<User> responseEntity = userController.updateUser(updatedUser, userId);

    // Assert
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(updatedUser, responseEntity.getBody());

    // Verify that userService.updateUser() was called once with the correct user and userId
    verify(userService, times(1)).updateUser(updatedUser, userId);
  }

  @Test
  void testGetAllUsers() {
    // Arrange
    List<User> userList = new ArrayList<>();
    User user1 = new User();
    user1.setId(1L);
    user1.setUsername("John");

    User user2 = new User();
    user2.setId(2L);
    user2.setUsername("Jane");

    userList.add(user1);
    userList.add(user2);

    UserService userService = mock(UserService.class);
    when(userService.getAllUsers()).thenReturn(userList);

    UserController userController = new UserController(userService, null, null);

    // Act
    ResponseEntity<List<User>> responseEntity = userController.getAllUsers();

    // Assert
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(userList, responseEntity.getBody());

    // Verify that userService.getAllUsers() was called once
    verify(userService, times(1)).getAllUsers();
  }

  @Test
  void testAddUserAndCountry() {
    // Arrange
    User userToAdd = new User(); // Создаем новый экземпляр User
    userToAdd.setId(1L); // Устанавливаем id с помощью сеттера
    userToAdd.setUsername("John"); // Устанавливаем username с помощью сеттера

    String countryName = "USA";

    // Создаем экземпляр UserDto и устанавливаем его значения с помощью сеттеров
    UserDto userDtoToAdd = new UserDto();
    userDtoToAdd.setId(userToAdd.getId());
    userDtoToAdd.setUsername(userToAdd.getUsername());

    UserWithCountryService userWithCountryService = mock(UserWithCountryService.class);
    when(userWithCountryService.addUserAndCountry(any(UserDto.class), eq(countryName)))
        .thenReturn(userDtoToAdd);

    UserController userController = new UserController(null, userWithCountryService, null);

    // Act
    ResponseEntity<UserDto> responseEntity =
        userController.addUserAndCountry(userDtoToAdd, countryName);

    // Assert
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(userDtoToAdd, responseEntity.getBody());

    // Verify that userWithCountryService.addUserAndCountry() was called once with the correct
    // arguments
    verify(userWithCountryService, times(1)).addUserAndCountry(any(UserDto.class), eq(countryName));
  }

  @Test
  void testGetAllUsersInCountry() {
    // Arrange
    Long countryId = 1L;
    List<UserDto> expectedUsers = new ArrayList<>();

    UserWithCountryService userWithCountryService = mock(UserWithCountryService.class);
    when(userWithCountryService.getAllUsersInCountry(countryId)).thenReturn(expectedUsers);

    UserController userController = new UserController(null, userWithCountryService, null);

    // Act
    ResponseEntity<List<UserDto>> responseEntity = userController.getAllUsersInCountry(countryId);

    // Assert
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(expectedUsers, responseEntity.getBody());

    // Verify that userWithCountryService.getAllUsersInCountry() was called once with the correct
    // countryId
    verify(userWithCountryService, times(1)).getAllUsersInCountry(countryId);
  }

  @Test
  void testAddCountryToUser() {
    // Arrange
    Long userId = 1L;
    Long countryId = 1L;
    UserDto expectedUser = new UserDto();

    UserWithCountryService userWithCountryService = mock(UserWithCountryService.class);
    when(userWithCountryService.addCountryToUser(userId, countryId)).thenReturn(expectedUser);

    UserController userController = new UserController(null, userWithCountryService, null);

    // Act
    ResponseEntity<UserDto> responseEntity = userController.addCountryToUser(userId, countryId);

    // Assert
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(expectedUser, responseEntity.getBody());

    // Verify that userWithCountryService.addCountryToUser() was called once with the correct userId
    // and countryId
    verify(userWithCountryService, times(1)).addCountryToUser(userId, countryId);
  }

  @Test
  void testRemoveCountryFromUser() {
    // Arrange
    Long userId = 1L;

    UserWithCountryService userWithCountryService = mock(UserWithCountryService.class);
    doNothing().when(userWithCountryService).removeCountryFromUser(userId);

    UserController userController = new UserController(null, userWithCountryService, null);

    // Act
    ResponseEntity<Void> responseEntity = userController.removeCountryFromUser(userId);

    // Assert
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

    // Verify that userWithCountryService.removeCountryFromUser() was called once with the correct
    // userId
    verify(userWithCountryService, times(1)).removeCountryFromUser(userId);
  }

  @Test
  void testUpdateUserCountry() {
    // Arrange
    Long userId = 1L;
    Long countryId = 1L;
    UserDto expectedUser = new UserDto();

    UserWithCountryService userWithCountryService = mock(UserWithCountryService.class);
    when(userWithCountryService.updateUserCountry(userId, countryId)).thenReturn(expectedUser);

    UserController userController = new UserController(null, userWithCountryService, null);

    // Act
    ResponseEntity<UserDto> responseEntity = userController.updateUserCountry(userId, countryId);

    // Assert
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(expectedUser, responseEntity.getBody());

    // Verify that userWithCountryService.updateUserCountry() was called once with the correct
    // userId and countryId
    verify(userWithCountryService, times(1)).updateUserCountry(userId, countryId);
  }

  @Test
  void testGetAllUsersWithCountries() {
    // Arrange
    List<UserDto> expectedUsers = new ArrayList<>();

    UserWithCountryService userWithCountryService = mock(UserWithCountryService.class);
    when(userWithCountryService.getAllUsersWithCountries()).thenReturn(expectedUsers);

    UserController userController = new UserController(null, userWithCountryService, null);

    // Act
    ResponseEntity<List<UserDto>> responseEntity = userController.getAllUsersWithCountries();

    // Assert
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(expectedUsers, responseEntity.getBody());

    // Verify that userWithCountryService.getAllUsersWithCountries() was called once
    verify(userWithCountryService, times(1)).getAllUsersWithCountries();
  }
}
