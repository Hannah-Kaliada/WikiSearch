package com.search.wiki.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private long id;
    private String username;
    private String email;
    private String password;
}
