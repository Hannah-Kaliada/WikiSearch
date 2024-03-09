package com.search.wiki.service;

import com.search.wiki.model.User;
import com.search.wiki.repository.UserDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserDAO repository;

    public User addUser(User user) {
        return repository.saveUser(user);
    }

    public User getUserById(long id) {
        return repository.findById(id);
    }

    public User updateUser(User user) {
        return repository.updateUser(user);
    }

    public boolean deleteUser(long id) {
        return repository.deleteUser(id);
    }

    public List<User> getAllUsers() {
        return repository.findAllUsers();
    }
}
