package com.search.wiki.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@Table(name = "users")
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue
    private long id;
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
}