package com.search.wiki.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @ManyToOne
    @JoinColumn(name = "country_id")
    @JsonBackReference
    private Country country;

}