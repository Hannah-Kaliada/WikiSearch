package com.search.wiki.entity_tests;

import static org.junit.jupiter.api.Assertions.*;

import com.search.wiki.entity.Query;
import org.junit.jupiter.api.Test;

class QueryTest {

  @Test
  void testConstructorAndGetters() {
    String searchTerm = "Test Search Term";
    Query query = new Query(searchTerm);
    assertEquals(searchTerm, query.getSearchTerm());
  }

  @Test
  void testEquals_SameInstance_ReturnsTrue() {
    Query query = new Query("Search Term");
    assertEquals(query, query);
  }

  @Test
  void testEquals_Null_ReturnsFalse() {
    Query query = new Query("Search Term");
    assertNotEquals(null, query);
  }

  @Test
  void testEquals_DifferentObjects_ReturnsFalse() {
    Query query1 = new Query("Search Term");
    Query query2 = new Query("Different Search Term");
    assertNotEquals(query1, query2);
  }

  @Test
  void testEquals_SameSearchTerm_ReturnsTrue() {
    Query query1 = new Query("Search Term");
    Query query2 = new Query("Search Term");
    assertEquals(query1, query2);
  }

  @Test
  void testHashCode_SameObjects_ReturnsSameHashCode() {
    Query query1 = new Query("Search Term");
    Query query2 = new Query("Search Term");
    assertEquals(query1.hashCode(), query2.hashCode());
  }

  @Test
  void testHashCode_DifferentSearchTerms_ReturnsDifferentHashCodes() {
    Query query1 = new Query("Search Term");
    Query query2 = new Query("Different Search Term");
    assertNotEquals(query1.hashCode(), query2.hashCode());
  }

  @Test
  void testSetters() {
    Query query = new Query("Search Term");
    String newSearchTerm = "New Search Term";
    query.setSearchTerm(newSearchTerm);
    assertEquals(newSearchTerm, query.getSearchTerm());
  }

  @Test
  void testEquals_WithEqualStrings_ReturnsTrue() {
    Query query1 = new Query("Search Term");
    Query query2 = new Query("Search Term");
    assertEquals(query1, query2);
  }

  @Test
  void testEquals_WithNull_ReturnsFalse() {
    Query query = new Query("Search Term");
    assertNotEquals(null, query);
  }
}
