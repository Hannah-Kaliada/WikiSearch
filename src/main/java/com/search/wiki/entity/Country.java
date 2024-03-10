package com.search.wiki.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "countries")
@Entity
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JsonManagedReference
    private List<User> users = new ArrayList<>();
}