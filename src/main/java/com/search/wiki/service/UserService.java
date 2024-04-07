package com.search.wiki.service;

import com.search.wiki.cache.Cache;
import com.search.wiki.entity.User;
import com.search.wiki.exceptions.customexceptions.NotFoundException;
import com.search.wiki.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** The type User service. */
@Service
@Transactional
public class UserService {
  private final UserRepository repository;
  private final Cache cache;
  private static final String USER_CACHE_PREFIX = "User_";
  private static final String IdRequired = "Id cannot be less than 1";

  /**
   * Instantiates a new User service.
   *
   * @param repository the repository
   * @param cache the cache
   */
  public UserService(UserRepository repository, Cache cache) {
    this.repository = repository;
    this.cache = cache;
  }

  /**
   * Add user user.
   *
   * @param user the user
   * @return the user
   */
  public User addUser(User user) {
    if (user == null) {
      throw new IllegalArgumentException("User cannot be null");
    } else if (repository.existsByUsername(user.getUsername())) {
      throw new IllegalArgumentException(
          "User already exists with username: " + user.getUsername());
    } else if (repository.existsByEmail(user.getEmail())) {
      throw new IllegalArgumentException("User already exists with email: " + user.getEmail());
    }
    User savedUser = repository.save(user);
    cache.put(getUserCacheKey(savedUser.getId()), savedUser);
    return savedUser;
  }

  /**
   * Gets user by id.
   *
   * @param id the id
   * @return the user by id
   */
  public User getUserById(long id) {
    if (id < 1) {
      throw new IllegalArgumentException(IdRequired);
    }
    String cacheKey = getUserCacheKey(id);
    return getCachedOrFromRepository(cacheKey, id);
  }

  /**
   * Update user user.
   *
   * @param user the user
   * @param id the id
   * @return the user
   */
  @Transactional
  public User updateUser(User user, long id) {
    if (id < 1) {
      throw new IllegalArgumentException(IdRequired);
    }
    String cacheKey = getUserCacheKey(id);
    User cachedUser = (User) cache.get(cacheKey);

    if (cachedUser != null) {
      cachedUser.setUsername(user.getUsername());
      cachedUser.setEmail(user.getEmail());
      cachedUser.setPassword(user.getPassword());
      cachedUser.setCountry(user.getCountry());
      cache.put(cacheKey, cachedUser);
      return cachedUser;
    } else {
      Optional<User> optionalUser = repository.findById(id);

      if (optionalUser.isPresent()) {
        user.setId(id);
        User updatedUser = repository.save(user);
        cache.put(cacheKey, updatedUser);
        return updatedUser;
      } else {
        throw new NotFoundException("User not found with id: " + id);
      }
    }
  }

  /**
   * Delete user boolean.
   *
   * @param userId the user id
   * @return the boolean
   */
  @Transactional
  public boolean deleteUser(long userId) {
    if (userId < 1) {
      throw new IllegalArgumentException(IdRequired);
    }
    if (repository.existsById(userId)) {
      repository.deleteById(userId);
      cache.remove(getUserCacheKey(userId));
      return true;
    }
    throw new NotFoundException("User not found with id: " + userId);
  }

  /**
   * Gets all users.
   *
   * @return the all users
   */
  public List<User> getAllUsers() {
    List<User> users = new ArrayList<>();
    Set<String> userCacheKeys = cache.getCacheKeysStartingWith(USER_CACHE_PREFIX);
    if (userCacheKeys.size() == repository.count()) {
      for (String cacheKey : userCacheKeys) {
        users.add((User) cache.get(cacheKey));
      }
      return users;
    }
    users = repository.findAll();
    for (User user : users) {
      String userCacheKey = getUserCacheKey(user.getId());
      cache.put(userCacheKey, user);
    }
    return users;
  }

  private String getUserCacheKey(long id) {
    return USER_CACHE_PREFIX + id;
  }

  private User getCachedOrFromRepository(String cacheKey, long id) {
    if (id < 1) {
      throw new IllegalArgumentException(IdRequired);
    }
    if (cache.containsKey(cacheKey)) {
      return (User) cache.get(cacheKey);
    } else {
      Optional<User> userOptional = repository.findById(id);
      if (userOptional.isPresent()) {
        User user = userOptional.get();
        cache.put(cacheKey, user);
        return user;
      }
      throw new NotFoundException("User not found with ID: " + id);
    }
  }
}
