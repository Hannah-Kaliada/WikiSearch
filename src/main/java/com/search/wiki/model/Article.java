package com.search.wiki.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue
    private long id;
    @Column
    private String title;
    private String url;
    private String snippet;
    private String imagePath;
}
