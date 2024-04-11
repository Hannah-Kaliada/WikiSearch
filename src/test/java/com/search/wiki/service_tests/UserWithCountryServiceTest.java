package com.search.wiki.service_tests;

import static org.junit.jupiter.api.Assertions.*;

import com.search.wiki.controller.dto.UserDto;
import com.search.wiki.service.UserWithCountryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserWithCountryServiceTest {
  @InjectMocks private UserWithCountryService userWithCountryService;

  @Test
  void testAddUserAndCountry_NullUserDto_ThrowsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> userWithCountryService.addUserAndCountry(null, "Test Country"));
  }

  @Test
  void testAddUserAndCountry_NullCountryName_ThrowsIllegalArgumentException() {
    UserDto userDto = new UserDto();
    userDto.setId(1L);
    userDto.setUsername("testuser");
    userDto.setEmail("testuser@example.com");
    userDto.setPassword("password");
    userDto.setCountry(null);

    assertThrows(
        IllegalArgumentException.class,
        () -> userWithCountryService.addUserAndCountry(userDto, null));
  }

  @Test
  void testGetAllUsersInCountry_InvalidCountryId_ThrowsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class, () -> userWithCountryService.getAllUsersInCountry(0L));
  }

  @Test
  void testAddCountryToUser_InvalidUserId_ThrowsNotFoundException() {
    assertThrows(
        IllegalArgumentException.class, () -> userWithCountryService.addCountryToUser(0L, 1L));
  }

  @Test
  void testAddCountryToUser_InvalidCountryId_ThrowsNotFoundException() {
    assertThrows(Exception.class, () -> userWithCountryService.addCountryToUser(1L, 0L));
  }
}
