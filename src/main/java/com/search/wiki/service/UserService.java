package com.search.wiki.service;

import com.search.wiki.entity.User;
import com.search.wiki.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.search.wiki.cache.Cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService {
    private final UserRepository repository;
    private final Cache cache;
    private static final String USER_CACHE_PREFIX = "User_";

    public UserService(UserRepository repository, Cache cache) {
        this.repository = repository;
        this.cache = cache;
    }

    public User addUser(User user) {
        User savedUser = repository.save(user);
        cache.put(getUserCacheKey(savedUser.getId()), savedUser);
        return savedUser;
    }

    public User getUserById(long id) {
        String cacheKey = getUserCacheKey(id);
        return getCachedOrFromRepository(cacheKey, id);
    }

    @Transactional
    public User updateUser(User user, long id) {
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
                return null;
            }
        }
    }

    @Transactional
    public boolean deleteUser(long userId) {
        if (repository.existsById(userId)) {
            repository.deleteById(userId);
            cache.remove(getUserCacheKey(userId));
            return true;
        }
        return false;
    }

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
        if (cache.containsKey(cacheKey)) {
            return (User) cache.get(cacheKey);
        } else {
            Optional<User> userOptional = repository.findById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                cache.put(cacheKey, user);
                return user;
            }
            return null;
        }
    }
}
