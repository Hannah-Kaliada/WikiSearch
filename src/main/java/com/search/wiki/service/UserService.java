package com.search.wiki.service;

import com.search.wiki.entity.User;
import com.search.wiki.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User addUser(User user) {
        return repository.save(user);
    }

    public User getUserById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public User updateUser(User user) {
        Optional<User> existingUserOptional = repository.findById(user.getId());

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            return repository.save(existingUser);
        }

        return null;
    }

    @Transactional
    public boolean deleteUser(long userId) {
        User user = repository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }
        repository.deleteById(userId);
        return true;
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }
}
