package com.search.wiki.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Country {
    private long id;
    private String name;
}
