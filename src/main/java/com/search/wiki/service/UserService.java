package com.search.wiki.service;

import com.search.wiki.controller.dto.UserDTO;
import com.search.wiki.entity.Country;
import com.search.wiki.entity.User;
import com.search.wiki.repository.UserRepository;
import com.search.wiki.service.utils.ConvertToDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;
    private final CountryService countryService;

    public UserService(UserRepository repository, CountryService countryService) {
        this.repository = repository;
        this.countryService = countryService;
    }
    public UserDTO addUserWithCountry(UserDTO userDTO, Long countryId) {
        // Создаем пользователя из DTO
        User user = ConvertToDTO.convertToUser(userDTO);

        // Проверяем наличие страны
        Country country = countryService.getCountryById(countryId);
        if (country == null) {
            // Если страны не существует, создаем новую страну
            country = new Country();
            country.setId(countryId); // Предполагается, что идентификатор страны задается в запросе
            countryService.addCountry(country);
        }

        // Привязываем пользователя к стране
        user.setCountry(country);

        // Сохраняем пользователя
        User savedUser = repository.save(user);
        if (savedUser == null) {
            return null;
        }

        // Преобразуем сохраненного пользователя в DTO
        return ConvertToDTO.convertUserToDTO(savedUser);
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
