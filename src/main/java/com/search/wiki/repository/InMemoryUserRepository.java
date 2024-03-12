package com.search.wiki.repository;

import com.search.wiki.entity.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryUserRepository {
    private final List<User> users = new ArrayList<>();

    public List<User> findAllUsers() {
        return users;
    }

    public User saveUser(User user) {
        users.add(user);
        return user;
    }

    public User findById(long id) {
        return users.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    }

    public User updateUser(User user) {
        Optional<User> existingUser = users.stream().filter(u -> u.getId() == user.getId()).findFirst();
        existingUser.ifPresent(u -> {
            u.setUsername(user.getUsername());
            u.setEmail(user.getEmail());
            u.setPassword(user.getPassword());
        });
        return user;
    }

    public boolean deleteUser(long id) {
        return users.removeIf(user -> user.getId() == id);
    }
}