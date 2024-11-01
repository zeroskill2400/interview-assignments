package org.zeroskill.common.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zeroskill.common.dto.UserDto;
import org.zeroskill.common.dto.request.UserUpdateRequest;
import org.zeroskill.common.entity.Gender;
import org.zeroskill.common.entity.User;
import org.zeroskill.common.exception.InterviewException;
import org.zeroskill.common.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateUser_ShouldReturnUsername_WhenCredentialsAreValid() {
        String username = "testUser";
        String password = "testPassword";
        User user = new User(username, "test@example.com", BCrypt.hashpw(password, BCrypt.gensalt()), 25, "1234567890", Gender.MALE);

        when(userRepository.findActiveUserByUsername(username)).thenReturn(Optional.of(user));

        Optional<String> validatedUsername = userService.validateUser(username, password);

        assertTrue(validatedUsername.isPresent());
        assertEquals(username, validatedUsername.get());

        verify(userRepository).findActiveUserByUsername(username);
    }

    @Test
    void validateUser_ShouldReturnEmpty_WhenCredentialsAreInvalid() {
        String username = "testUser";
        String password = "wrongPassword";
        User user = new User(username, "test@example.com", BCrypt.hashpw("testPassword", BCrypt.gensalt()), 25, "1234567890", Gender.MALE);

        when(userRepository.findActiveUserByUsername(username)).thenReturn(Optional.of(user));

        Optional<String> validatedUsername = userService.validateUser(username, password);

        assertTrue(validatedUsername.isEmpty());

        verify(userRepository).findActiveUserByUsername(username);
    }

    @Test
    void getUser_ShouldReturnUserDto_WhenUserExists() {
        Long userId = 1L;
        User user = new User("testUser", "test@example.com", "hashedPassword", 25, "1234567890", Gender.MALE);
        user.updateId(userId);

        when(userRepository.findActiveUserById(userId)).thenReturn(Optional.of(user));

        UserDto userDto = userService.getUser(userId);

        assertEquals(user.getId(), userDto.id());
        assertEquals(user.getUsername(), userDto.username());
        assertEquals(user.getEmail(), userDto.email());

        verify(userRepository).findActiveUserById(userId);
    }

    @Test
    void getUser_ShouldThrowException_WhenUserNotFound() {
        Long userId = 1L;

        when(userRepository.findActiveUserById(userId)).thenReturn(Optional.empty());

        assertThrows(InterviewException.class, () -> userService.getUser(userId));

        verify(userRepository).findActiveUserById(userId);
    }

    @Test
    void addUser_ShouldReturnUserDto() {
        UserDto userDto = new UserDto(null, "newUser", "new@example.com", "password", 25, "1234567890", Gender.MALE, null, null);
        User user = User.toEntity(userDto);
        user.updateId(1L);

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto savedUserDto = userService.addUser(userDto);

        assertEquals(user.getId(), savedUserDto.id());
        assertEquals(user.getUsername(), savedUserDto.username());
        assertEquals(user.getEmail(), savedUserDto.email());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUserDto() {
        Long userId = 1L;
        UserUpdateRequest request = new UserUpdateRequest("updatedUser", "newPassword", "updated@example.com", 30, "0987654321", Gender.FEMALE);
        User user = new User("testUser", "test@example.com", "hashedPassword", 25, "1234567890", Gender.MALE);
        user.updateId(userId);

        when(userRepository.findActiveUserById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto updatedUserDto = userService.updateUser(request, userId);

        assertEquals(request.username(), updatedUserDto.username());
        assertEquals(request.email(), updatedUserDto.email());
        assertEquals(request.age(), updatedUserDto.age());
        assertEquals(request.phone(), updatedUserDto.phone());
        assertEquals(request.gender(), updatedUserDto.gender());

        verify(userRepository).findActiveUserById(userId);
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        Long userId = 1L;
        UserUpdateRequest request = new UserUpdateRequest("updatedUser", "newPassword", "updated@example.com", 30, "0987654321", Gender.FEMALE);

        when(userRepository.findActiveUserById(userId)).thenReturn(Optional.empty());

        assertThrows(InterviewException.class, () -> userService.updateUser(request, userId));

        verify(userRepository).findActiveUserById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_ShouldMarkUserAsDeleted() {
        Long userId = 1L;
        User user = new User("testUser", "test@example.com", "hashedPassword", 25, "1234567890", Gender.MALE);
        user.updateId(userId);

        when(userRepository.findActiveUserById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        assertTrue(user.isDeleted());

        verify(userRepository).findActiveUserById(userId);
        verify(userRepository).save(user);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserNotFound() {
        Long userId = 1L;

        when(userRepository.findActiveUserById(userId)).thenReturn(Optional.empty());

        assertThrows(InterviewException.class, () -> userService.deleteUser(userId));

        verify(userRepository).findActiveUserById(userId);
        verify(userRepository, never()).save(any(User.class));
    }
}