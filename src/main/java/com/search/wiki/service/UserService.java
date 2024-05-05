package com.search.wiki.service;

import com.search.wiki.cache.Cache;
import com.search.wiki.entity.User;
import com.search.wiki.exceptions.ExceptionConstants;
import com.search.wiki.exceptions.customexceptions.DuplicateEntryException;
import com.search.wiki.exceptions.customexceptions.NotFoundException;
import com.search.wiki.repository.UserRepository;
import com.search.wiki.service.utils.RequestCountService;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** The type User service. */
@Service
@Transactional
public class UserService {
  private final UserRepository repository;
  private final Cache cache;
  private static final String USER_CACHE_PREFIX = "User_";
  private final RequestCountService requestCountService;
    @PersistenceContext
    private EntityManager entityManager;

    public long getUserIdByEmailAndPassword(String email, String password) {
        try {
            return entityManager.createQuery(
                            "SELECT u.id FROM User u WHERE u.email = :email AND u.password = :password", Long.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (NoResultException e) {
            // Если пользователь с таким email и password не найден, возвращаем 0 или можно обработать исключение по вашему усмотрению
            return 0;
        }
    }

  /**
   * Instantiates a new User service.
   *
   * @param repository the repository
   * @param cache the cache
   * @param requestCountService the request count service
   */
public UserService(
      UserRepository repository, Cache cache, RequestCountService requestCountService) {
    this.repository = repository;
    this.cache = cache;
    this.requestCountService = requestCountService;
  }

  /**
   * Add user user.
   *
   * @param user the user
   * @return the user
   */
public User addUser(User user) {
    requestCountService.incrementRequestCount();
    if (user == null) {
      throw new IllegalArgumentException("User cannot be null");
    } else if (repository.existsByUsername(user.getUsername())) {
      throw new DuplicateEntryException(
          "User already exists with username: " + user.getUsername());
    } else if (repository.existsByEmail(user.getEmail())) {
      throw new DuplicateEntryException("User already exists with email: " + user.getEmail());
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
    requestCountService.incrementRequestCount();
    if (id < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
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
    requestCountService.incrementRequestCount();
    if (id < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
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
        throw new NotFoundException(ExceptionConstants.USER_NOT_FOUND + id);
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
    requestCountService.incrementRequestCount();
    if (userId < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
    }
    if (repository.existsById(userId)) {
      repository.deleteById(userId);
      cache.remove(getUserCacheKey(userId));
      return true;
    }
    throw new NotFoundException(ExceptionConstants.USER_NOT_FOUND + userId);
  }

  /**
   * Gets all users.
   *
   * @return the all users
   */
public List<User> getAllUsers() {
    requestCountService.incrementRequestCount();
    Set<String> userCacheKeys = cache.getCacheKeysStartingWith(USER_CACHE_PREFIX);

    List<User> users = userCacheKeys.stream().map(cacheKey -> (User) cache.get(cacheKey)).toList();

    if (users.size() == repository.count()) {
      return users;
    }

    users = repository.findAll();
    users.forEach(user -> cache.put(getUserCacheKey(user.getId()), user));

    return users;
  }


  private String getUserCacheKey(long id) {
    return USER_CACHE_PREFIX + id;
  }

  private User getCachedOrFromRepository(String cacheKey, long id) {
    if (id < 1) {
      throw new IllegalArgumentException(ExceptionConstants.ID_REQUIRED);
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
      throw new NotFoundException(ExceptionConstants.USER_NOT_FOUND + id);
    }
  }

  /**
   * Gets request count.
   *
   * @return the request count
   */
  public int getRequestCount() {
    return requestCountService.getRequestCount();
  }
}
