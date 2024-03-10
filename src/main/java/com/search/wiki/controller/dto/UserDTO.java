package com.search.wiki.controller.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private CountryDTO country;
    private String password;
}

