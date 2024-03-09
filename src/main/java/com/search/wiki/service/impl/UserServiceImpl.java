package com.search.wiki.service.impl;

import com.search.wiki.model.Country;
import com.search.wiki.model.User;
import com.search.wiki.repository.UserRepository;
import com.search.wiki.service.CountryService;
import com.search.wiki.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Primary
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final CountryService countryService;

    @Override
    public User addUser(User user) {
        Country country = countryService.getCountryByName(user.getCountry().getName());
        user.setCountry(country);
        return repository.save(user);
    }

    @Override
    public User getUserById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public User updateUser(User user) {
        Country country = countryService.getCountryByName(user.getCountry().getName());
        user.setCountry(country);
        return repository.save(user);
    }

    @Override
    @Transactional
    public boolean deleteUser(long userId) {
        User user = repository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }
        repository.deleteById(userId);
        return true;
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }
}