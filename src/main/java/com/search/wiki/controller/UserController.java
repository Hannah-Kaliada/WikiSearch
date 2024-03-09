package com.search.wiki.controller;

import com.search.wiki.controller.dto.CountryDTO;
import com.search.wiki.controller.dto.UserDTO;
import com.search.wiki.entity.Country;
import com.search.wiki.entity.User;
import com.search.wiki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsersWithCountries() {
        List<User> users = userService.getAllUsersWithCountries();
        List<UserDTO> userDTOList = convertToDTOList(users);
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }




    public List<UserDTO> convertToDTOList(List<User> users) {
        return users.stream()
                .map(user -> {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId(user.getId());
                    userDTO.setUsername(user.getUsername());
                    userDTO.setEmail(user.getEmail());
                    userDTO.setPassword(user.getPassword());

                    // Создаем объект CountryDTO
                    CountryDTO countryDTO = new CountryDTO();
                    Country country = user.getCountry();
                    if (country != null) {
                        countryDTO.setId(country.getId());
                        countryDTO.setName(country.getName());
                    }

                    userDTO.setCountry(countryDTO);

                    return userDTO;
                })
                .toList();
    }



    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/addUser")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("updateUser")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("deleteUser/{id}")
    public boolean deleteUser(@PathVariable long id) {
        return userService.deleteUser(id);
    }
    @GetMapping("/userInfo/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        UserDTO userDTO = convertToDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());

        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(user.getCountry().getId());
        countryDTO.setName(user.getCountry().getName());

        userDTO.setCountry(countryDTO);
        return userDTO;
    }
}