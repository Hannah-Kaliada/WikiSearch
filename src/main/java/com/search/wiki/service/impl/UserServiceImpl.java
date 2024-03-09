package com.search.wiki.service.impl;

import com.search.wiki.entity.Country;
import com.search.wiki.entity.User;
import com.search.wiki.repository.UserRepository;
import com.search.wiki.service.CountryService;
import com.search.wiki.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    @Transactional
    public User updateUser(User user) {
        Optional<User> existingUserOptional = repository.findById(user.getId());

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            Country country = countryService.getCountryByName(user.getCountry().getName());

            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setCountry(country);

            return repository.save(existingUser);
        }

        return null;
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

    @Override
    @Transactional
    @EntityGraph(attributePaths = "country")
    public List<User> getAllUsersWithCountries() {
        return repository.findAll();
    }
}