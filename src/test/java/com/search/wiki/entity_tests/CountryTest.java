package com.search.wiki.entity_tests;

import static org.junit.jupiter.api.Assertions.*;

import com.search.wiki.entity.Country;
import com.search.wiki.entity.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CountryTest {

  @Test
  void testEquals_SameInstance_ReturnsTrue() {
    Country country = new Country(1L, "CountryName");
    assertEquals(country, country);
  }

  @Test
  void testEquals_Null_ReturnsFalse() {
    Country country = new Country(1L, "CountryName");
    assertNotEquals(null, country);
  }

  @Test
  void testEquals_DifferentObjects_ReturnsFalse() {
    Country country1 = new Country(1L, "CountryName");
    Country country2 = new Country(2L, "DifferentCountry");
    assertNotEquals(country1, country2);
  }

  @Test
  void testHashCode_DifferentObjects_ReturnsDifferentHashCode() {
    Country country1 = new Country(1L, "CountryName");
    Country country2 = new Country(2L, "DifferentCountry");
    assertNotEquals(country1.hashCode(), country2.hashCode());
  }

  @Test
  void testConstructorAndGetters() {
    long id = 1L;
    String name = "CountryName";

    Country country = new Country(id, name);

    assertEquals(id, country.getId());
    assertEquals(name, country.getName());
  }

  @Test
  void testSettersAndGetters() {
    Country country = new Country();
    long id = 1L;
    String name = "CountryName";

    country.setId(id);
    country.setName(name);

    assertEquals(id, country.getId());
    assertEquals(name, country.getName());
  }

  @Test
  void testUsersList() {
    Country country = new Country(1L, "CountryName");
    User user1 = new User();
    User user2 = new User();
    user1.setId(1L);
    user2.setId(2L);

    List<User> users = new ArrayList<>();
    users.add(user1);
    users.add(user2);

    country.setUsers(users);

    assertEquals(users, country.getUsers());
    assertEquals(2, country.getUsers().size());
    assertTrue(country.getUsers().contains(user1));
    assertTrue(country.getUsers().contains(user2));
  }

  @Test
  void testEquals_SameId_ReturnsTrue() {
    Country country1 = new Country(1L, "CountryName");
    Country country2 = new Country(1L, "DifferentCountry");
    assertEquals(country1, country2);
  }

  @Test
  void testEquals_DifferentId_ReturnsFalse() {
    Country country1 = new Country(1L, "CountryName");
    Country country2 = new Country(2L, "CountryName");
    assertNotEquals(country1, country2);
  }
}
