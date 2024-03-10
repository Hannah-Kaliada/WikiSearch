package com.search.wiki.service;

import com.search.wiki.entity.User;

import java.util.List;

public interface
UserService {
    User addUser(User user);
    User getUserById(long id);
    User updateUser(User user);
    boolean deleteUser(long id);
    List<User> getAllUsers();
}
