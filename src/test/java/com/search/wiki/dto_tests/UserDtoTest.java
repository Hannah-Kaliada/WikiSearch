package com.search.wiki.dto_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.search.wiki.controller.dto.CountryDto;
import com.search.wiki.controller.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDtoTest {

  private UserDto user1;
  private UserDto user2;

  @BeforeEach
  void setUp() {
    user1 = new UserDto();
    user1.setId(1L);
    user1.setUsername("user1");
    user1.setEmail("user1@example.com");
    user1.setCountry(new CountryDto());
    user1.setPassword("password");

    user2 = new UserDto();
    user2.setId(1L);
    user2.setUsername("user1");
    user2.setEmail("user1@example.com");
    user2.setCountry(new CountryDto());
    user2.setPassword("password");
  }

  @Test
  void testEquals_SameInstance_ReturnsTrue() {
    assertEquals(user1, user1);
  }

  @Test
  void testEquals_Null_ReturnsFalse() {
    assertNotEquals(null, user1);
  }

  @Test
  void testEquals_DifferentClass_ReturnsFalse() {
    assertNotEquals("SomeString", user1);
  }

  @Test
  void testEquals_EqualObjects_ReturnsTrue() {
    assertEquals(user1, user2);
  }

  @Test
  void testEquals_DifferentId_ReturnsFalse() {
    user2.setId(2L);
    assertNotEquals(user1, user2);
  }

  @Test
  void testEquals_DifferentUsername_ReturnsFalse() {
    user2.setUsername("user2");
    assertNotEquals(user1, user2);
  }

  @Test
  void testHashCode_EqualObjects_ReturnsSameHashCode() {
    assertEquals(user1.hashCode(), user2.hashCode());
  }

  @Test
  void testHashCode_DifferentObjects_ReturnsDifferentHashCode() {
    user2.setUsername("user2");
    assertNotEquals(user1.hashCode(), user2.hashCode());
  }

  @Test
  void testHashCode_SameObjects_ReturnsConsistentHashCode() {
    int hashCode1 = user1.hashCode();
    int hashCode2 = user1.hashCode();
    assertEquals(hashCode1, hashCode2);
  }

  @Test
  void testHashCode_ObjectsWithSameValues_ReturnsSameHashCode() {
    UserDto user3 = new UserDto();
    user3.setId(1L);
    user3.setUsername("user1");
    user3.setEmail("user1@example.com");
    user3.setCountry(new CountryDto());
    user3.setPassword("password");
    assertEquals(user1.hashCode(), user3.hashCode());
  }

  @Test
  void testGetterAndSetter() {
    CountryDto newCountry = new CountryDto();
    newCountry.setName("NewCountry");
    user1.setCountry(newCountry);

    assertEquals(1L, user1.getId());
    assertEquals("user1", user1.getUsername());
    assertEquals("user1@example.com", user1.getEmail());
    assertEquals(newCountry, user1.getCountry());
    assertEquals("password", user1.getPassword());
  }
}
