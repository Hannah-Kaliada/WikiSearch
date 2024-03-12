package com.search.wiki.service.impl;

import com.search.wiki.entity.User;
import com.search.wiki.repository.InMemoryUserRepository;
import com.search.wiki.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InMemoryUserServiceImpl implements UserService {

    private final InMemoryUserRepository repository;

    public InMemoryUserServiceImpl(InMemoryUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User addUser(User user) {
        return repository.saveUser(user);
    }

    @Override
    public User getUserById(long id) {
        return repository.findById(id);
    }

    @Override
    public User updateUser(User user) {
        return repository.updateUser(user);
    }

    @Override
    public boolean deleteUser(long id) {
        return repository.deleteUser(id);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAllUsers();
    }
}
