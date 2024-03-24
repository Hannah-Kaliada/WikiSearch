package com.search.wiki.controller.dto;

import lombok.Data;

@Data
public class ArticleDTO {
    private Long id;
    private String title;
    private String url;
    private String imagePath;
}
