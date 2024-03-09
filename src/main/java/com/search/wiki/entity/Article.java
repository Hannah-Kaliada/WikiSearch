package com.search.wiki.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@Table(name = "articles")
@DynamicUpdate
public class Article {
    @Id
    @GeneratedValue
    private long id;
    @Column
    private String title;
    private String url;
    private String imagePath;
}
