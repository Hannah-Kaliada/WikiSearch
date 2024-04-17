package com.search.wiki.entity;

import java.util.Objects;

public class Query {
  private String searchTerm;

  public Query() {
    // Пустой конструктор
  }

  public Query(String searchTerm) {
    this.searchTerm = searchTerm;
  }

  public String getSearchTerm() {
    return searchTerm;
  }

  public void setSearchTerm(String searchTerm) {
    this.searchTerm = searchTerm;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Query query = (Query) o;
    return Objects.equals(searchTerm, query.searchTerm);
  }

  @Override
  public int hashCode() {
    return Objects.hash(searchTerm);
  }
}
