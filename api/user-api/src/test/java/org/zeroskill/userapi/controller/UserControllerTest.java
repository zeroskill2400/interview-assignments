package org.zeroskill.userapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.zeroskill.common.dto.UserDto;
import org.zeroskill.common.dto.request.UserCreateRequest;
import org.zeroskill.common.dto.request.UserUpdateRequest;
import org.zeroskill.common.entity.Gender;
import org.zeroskill.common.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void addUser_ShouldReturnUserDto() throws Exception {
        // 유효한 UserCreateRequest 생성
        UserCreateRequest request = new UserCreateRequest("newUser", "password123", "new@example.com", 25, "1234567890", Gender.MALE);
        UserDto userDto = new UserDto(null, "newUser", "new@example.com", "hashedPassword", 25, "1234567890", Gender.MALE, null, null);
        UserDto savedUserDto = new UserDto(1L, "newUser", "new@example.com", null, 25, "1234567890", Gender.MALE, null, null);

        // 서비스 모의 응답 설정
        when(userService.addUser(any(UserDto.class))).thenReturn(savedUserDto);

        // POST 요청과 응답 검증
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.id").value(savedUserDto.id()))
                .andExpect(jsonPath("$.body.username").value(savedUserDto.username()))
                .andExpect(jsonPath("$.body.email").value(savedUserDto.email()));

        // 서비스 메서드 호출 확인
        verify(userService).addUser(any(UserDto.class));
    }

    @Test
    void getUser_ShouldReturnUserDto() throws Exception {
        Long userId = 1L;
        UserDto userDto = new UserDto(userId, "testUser", "test@example.com", null, 25, "1234567890", Gender.MALE, null, null);

        when(userService.getUser(userId)).thenReturn(userDto);

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.id").value(userDto.id()))
                .andExpect(jsonPath("$.body.username").value(userDto.username()))
                .andExpect(jsonPath("$.body.email").value(userDto.email()));

        verify(userService).getUser(userId);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUserDto() throws Exception {
        Long userId = 1L;
        UserUpdateRequest request = new UserUpdateRequest("updatedUser", "newPassword", "updated@example.com", 30, "0987654321", Gender.FEMALE);
        UserDto updatedUserDto = new UserDto(userId, "updatedUser", "updated@example.com", null, 30, "0987654321", Gender.FEMALE, null, null);

        when(userService.updateUser(any(UserUpdateRequest.class), eq(userId))).thenReturn(updatedUserDto);

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.id").value(updatedUserDto.id()))
                .andExpect(jsonPath("$.body.username").value(updatedUserDto.username()))
                .andExpect(jsonPath("$.body.email").value(updatedUserDto.email()));

        verify(userService).updateUser(any(UserUpdateRequest.class), eq(userId));
    }

    @Test
    void deleteUser_ShouldReturnOkStatus() throws Exception {
        Long userId = 1L;

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk());

        verify(userService).deleteUser(userId);
    }
}