package com.search.wiki.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** The type Query. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Query {
  private String searchTerm;
}
