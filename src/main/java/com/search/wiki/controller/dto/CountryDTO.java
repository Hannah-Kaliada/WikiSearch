package com.search.wiki.controller.dto;

import lombok.Data;

@Data
public class CountryDTO {
    private Long id;
    private String name;

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
