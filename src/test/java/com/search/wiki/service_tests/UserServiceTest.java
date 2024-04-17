package com.search.wiki.service_tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.search.wiki.cache.Cache;
import com.search.wiki.entity.User;
import com.search.wiki.exceptions.customexceptions.DuplicateEntryException;
import com.search.wiki.exceptions.customexceptions.NotFoundException;
import com.search.wiki.repository.UserRepository;
import com.search.wiki.service.UserService;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

/** The type User service test. */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private Cache cache;

  @InjectMocks private UserService userService;

  /** Test add user success. */
  @Test
  void testAddUser_Success() {
    when(userRepository.existsByUsername(anyString())).thenReturn(false);
    when(userRepository.existsByEmail(anyString())).thenReturn(false);

    User expectedUser = new User(1L, "testuser", "test@example.com", "password", null);

    when(userRepository.save(any(User.class))).thenReturn(expectedUser);

    User savedUser = userService.addUser(expectedUser);

    assertNotNull(savedUser);

    assertEquals(expectedUser.getId(), savedUser.getId());

    verify(cache).put("User_1", savedUser);
  }

  /** Test create user failure invalid data. */
  @Test
  void testCreateUser_Failure_InvalidData() {

    User invalidUser = new User(1L, null, "test@example.com", "password", null);

    assertThrows(NullPointerException.class, () -> userService.addUser(invalidUser));
  }

  /** Test add user duplicate username. */
  @Test
  void testAddUser_DuplicateUsername() {

    when(userRepository.existsByUsername(anyString())).thenReturn(true);

    User testUser = new User(1L, "testuser", "test@example.com", "password", null);
    assertThrows(DuplicateEntryException.class, () -> userService.addUser(testUser));
  }

  /** Test get user by id success. */
  @Test
  void testGetUserById_Success() {

    long userId = 1L;
    User user = new User(userId, "testuser", "test@example.com", "password", null);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    User resultUser = userService.getUserById(userId);

    assertNotNull(resultUser);
    assertEquals(user.getId(), resultUser.getId());
    assertEquals(user.getUsername(), resultUser.getUsername());
    assertEquals(user.getEmail(), resultUser.getEmail());
    assertEquals(user.getPassword(), resultUser.getPassword());
  }

  /** Test get user by id not found. */
  @Test
  void testGetUserById_NotFound() {
    long userId = 999L;

    when(cache.containsKey("User_" + userId)).thenReturn(false);
    when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

    assertThrows(NotFoundException.class, () -> userService.getUserById(userId));
  }

  /** Test update user success. */
  @Test
  void testUpdateUser_Success() {
    long userId = 1L;
    User existingUser = new User(userId, "testuser", "test@example.com", "password", null);
    User updatedUser = new User(userId, "updateduser", "updated@example.com", "newpassword", null);

    when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
    when(userRepository.save(updatedUser)).thenReturn(updatedUser);

    User resultUser = userService.updateUser(updatedUser, userId);

    assertNotNull(resultUser);
    assertEquals(updatedUser.getId(), resultUser.getId());
    assertEquals(updatedUser.getUsername(), resultUser.getUsername());
    assertEquals(updatedUser.getEmail(), resultUser.getEmail());
    assertEquals(updatedUser.getPassword(), resultUser.getPassword());
    verify(cache).put("User_" + userId, updatedUser);
  }

  /** Test update user user not found. */
  @Test
  void testUpdateUser_UserNotFound() {

    long userId = 999L;
    User updatedUser = new User(userId, "updateduser", "updated@example.com", "newpassword", null);

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> userService.updateUser(updatedUser, userId));
  }

  /** Test delete user success. */
  @Test
  void testDeleteUser_Success() {

    long userId = 1L;

    when(userRepository.existsById(userId)).thenReturn(true);

    boolean result = userService.deleteUser(userId);

    assertTrue(result);
    verify(userRepository).deleteById(userId);
    verify(cache).remove("User_" + userId);
  }

  /** Test get all users success. */
  @Test
  void testGetAllUsers_Success() {

    List<User> usersFromRepository = new ArrayList<>();
    usersFromRepository.add(new User(1L, "user1", "user1@example.com", "password1", null));
    usersFromRepository.add(new User(2L, "user2", "user2@example.com", "password2", null));

    when(userRepository.findAll()).thenReturn(usersFromRepository);
    when(cache.getCacheKeysStartingWith("User_")).thenReturn(Set.of("User_1", "User_2"));

    List<User> resultUsers = userService.getAllUsers();

    assertNotNull(resultUsers);
    assertEquals(usersFromRepository.size(), resultUsers.size());

    for (User user : usersFromRepository) {
      verify(cache).put("User_" + user.getId(), user);
    }
  }

  /** Test delete user user not found. */
  @Test
  void testDeleteUser_UserNotFound() {
    long userId = 999L;

    lenient()
        .doThrow(new EmptyResultDataAccessException(1))
        .when(userRepository)
        .deleteById(userId);

    assertThrows(NotFoundException.class, () -> userService.deleteUser(userId));
  }

  /** Test add user null user throws illegal argument exception. */
  @Test
  void testAddUser_NullUser_ThrowsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          userService.addUser(null);
        });
  }

  /** Test get user by id invalid id throws illegal argument exception. */
  @Test
  void testGetUserById_InvalidId_ThrowsIllegalArgumentException() {
    long invalidId = 0L;

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          userService.getUserById(invalidId);
        });
  }

  /** Test update user invalid id throws illegal argument exception. */
  @Test
  void testUpdateUser_InvalidId_ThrowsIllegalArgumentException() {
    long invalidId = 0L;
    User user = new User(1L, "testuser", "test@example.com", "password", null);

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          userService.updateUser(user, invalidId);
        });
  }

  /** Test delete user invalid id throws illegal argument exception. */
  @Test
  void testDeleteUser_InvalidId_ThrowsIllegalArgumentException() {
    long invalidId = 0L;

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          userService.deleteUser(invalidId);
        });
  }

  /** Test get all users from repository when cache is empty. */
  @Test
  void testGetAllUsers_FromRepositoryWhenCacheIsEmpty() {
    // Prepare mock data
    User user1 = new User(1L, "user1", "user1@example.com", "password1", null);
    User user2 = new User(2L, "user2", "user2@example.com", "password2", null);
    List<User> repositoryUsers = Arrays.asList(user1, user2);

    when(cache.getCacheKeysStartingWith("User_")).thenReturn(Collections.emptySet());
    when(userRepository.findAll()).thenReturn(repositoryUsers);
    when(userRepository.count()).thenReturn(2L);

    List<User> resultUsers = userService.getAllUsers();

    assertNotNull(resultUsers);
    assertEquals(2, resultUsers.size());
    assertTrue(resultUsers.contains(user1));
    assertTrue(resultUsers.contains(user2));
    verify(cache).getCacheKeysStartingWith("User_");
    verify(userRepository).findAll();
    for (User user : repositoryUsers) {
      verify(cache).put("User_" + user.getId(), user);
    }
  }
}
