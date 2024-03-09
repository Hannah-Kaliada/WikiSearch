package com.search.wiki.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private long id;
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
}