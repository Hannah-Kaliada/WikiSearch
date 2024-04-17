package com.search.wiki.service_tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.search.wiki.cache.Cache;
import com.search.wiki.controller.dto.CountryDto;
import com.search.wiki.controller.dto.UserDto;
import com.search.wiki.entity.Country;
import com.search.wiki.entity.User;
import com.search.wiki.repository.CountryRepository;
import com.search.wiki.repository.UserRepository;
import com.search.wiki.service.CountryService;
import com.search.wiki.service.UserService;
import com.search.wiki.service.UserWithCountryService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class UserWithCountryServiceTest {

  @Mock private UserService userService;

  @Mock CountryService countryService;

  @Mock Cache userCache;

  @Mock Cache countryCache;

  @Mock UserRepository userRepository;

  @Mock CountryRepository countryRepository;

  @InjectMocks UserWithCountryService userWithCountryService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testAddUserAndCountry_Success() {
    // Arrange
    String countryName = "USA";

    // Mock country retrieval by name
    when(countryService.getCountryIdByName(countryName)).thenReturn(1L);

    // Mock country retrieval by id
    Country country = new Country(1L, countryName);
    when(countryService.getCountryById(1L)).thenReturn(country);

    // Mock adding user
    UserDto userDto = new UserDto();
    userDto.setUsername("john_doe");
    userDto.setEmail("john@example.com");
    userDto.setPassword("password");
    userDto.setCountry(new CountryDto(1L, countryName));

    User userToAdd = new User("john_doe", "john@example.com", "password", country);
    when(userService.addUser(any(User.class))).thenReturn(userToAdd);

    // Act
    UserDto result = userWithCountryService.addUserAndCountry(userDto, countryName);

    // Assert
    assertNotNull(result);
    assertEquals(userDto.getUsername(), result.getUsername());
    assertEquals(userDto.getEmail(), result.getEmail());
    assertEquals(userDto.getPassword(), result.getPassword());
    assertNotNull(result.getCountry());
    assertEquals(1L, result.getCountry().getId());
    assertEquals(countryName, result.getCountry().getName());

    // Verify interactions
    verify(countryService, times(1)).getCountryIdByName(countryName);
    verify(countryService, times(1)).getCountryById(1L);
    verify(userService, times(1)).addUser(any(User.class));
  }

  @Test
  void testAddUserAndCountry_NullCountryName_ThrowsException() {
    // Arrange
    UserDto userDto = new UserDto();
    userDto.setUsername("john_doe");
    userDto.setEmail("john@example.com");
    userDto.setPassword("password");
    userDto.setCountry(null); // Устанавливаем CountryDto как null

    // Действие и проверка исключения
    assertThrows(
        IllegalArgumentException.class,
        () -> userWithCountryService.addUserAndCountry(userDto, null));
  }


  @Test
  void testAddCountryToUser_InvalidUserId_ThrowsException() {
    // Arrange
    Long invalidUserId = -1L;
    Long countryId = 2L;

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> userWithCountryService.addCountryToUser(invalidUserId, countryId));
  }

  @Test
  void testRemoveCountryFromUser_Success() {
    // Arrange
    Long userId = 1L;

    User existingUser = new User();
    existingUser.setId(userId);
    existingUser.setCountry(new Country());

    when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
    when(userCache.get("User_" + userId))
        .thenReturn(existingUser); // Установка ожидаемого поведения
    doNothing().when(userCache).put(eq("User_1"), isNull());

    // Act
    userWithCountryService.removeCountryFromUser(userId);

    // Assert
    assertNull(existingUser.getCountry());
    verify(userRepository, times(1)).save(existingUser);
  }

  @Test
  void testUpdateUserCountry_InvalidCountryId_ThrowsException() {
    // Arrange
    Long userId = 1L;
    Long invalidCountryId = -1L;

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> userWithCountryService.updateUserCountry(userId, invalidCountryId));
  }

		@Test
		void testGetAllUsersWithCountries_Success() {
				// Подготовка тестовых данных
				User user1 = new User(1L, "john_doe", "john@example.com", "password", new Country(1L, "USA"));
				User user2 = new User(2L, "jane_doe", "jane@example.com", "password", new Country(1L, "USA"));

				List<User> users = new ArrayList<>();
				users.add(user1);
				users.add(user2);

				// Мокирование userService для возврата списка пользователей
				UserService userService = mock(UserService.class);
				when(userService.getAllUsers()).thenReturn(users);

				// Мокирование countryService для возврата страны по ID
				CountryService countryService = mock(CountryService.class);
				Country country = new Country(1L, "USA");
				when(countryService.getCountryById(1L)).thenReturn(country);

				// Создание экземпляра UserWithCountryService с моками userService и countryService
				UserWithCountryService userWithCountryService = new UserWithCountryService(
								userService, countryService, userCache, countryCache, userRepository, countryRepository
				);

				// Вызов метода, который тестируем
				List<UserDto> result = userWithCountryService.getAllUsersWithCountries();

				// Проверка результатов
				assertNotNull(result);
				assertEquals(2, result.size());
				assertEquals(user1.getUsername(), result.get(0).getUsername());
				assertEquals(user2.getUsername(), result.get(1).getUsername());
				assertEquals(country.getName(), result.get(0).getCountry().getName());
				assertEquals(country.getName(), result.get(1).getCountry().getName());

				// Проверка вызовов методов
				verify(userService, times(1)).getAllUsers();
				verify(countryService, times(2)).getCountryById(1L); // Метод должен вызываться дважды для каждого пользователя
		}
}
