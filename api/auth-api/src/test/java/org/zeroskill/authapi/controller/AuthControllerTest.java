package org.zeroskill.authapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.zeroskill.authapi.dto.request.AuthRequest;
import org.zeroskill.common.exceptionhandler.GlobalExceptionHandler;
import org.zeroskill.common.service.UserService;
import org.zeroskill.common.util.AuthUtil;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.zeroskill.common.util.AuthUtil.makeJwt;
import static org.zeroskill.common.util.AuthUtil.validateJwt;

class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler()) // GlobalExceptionHandler 추가
                .build();
    }

    @Test
    void login_ShouldReturnJwtToken_WhenCredentialsAreValid() throws Exception {
        AuthRequest request = new AuthRequest("validUser", "validPassword");
        String mockToken = "mocked.jwt.token";

        when(userService.validateUser(request.userName(), request.password())).thenReturn(Optional.of("validUser"));

        try (MockedStatic<AuthUtil> mockStatic = mockStatic(AuthUtil.class)) {
            mockStatic.when(() -> makeJwt(request.userName())).thenReturn(mockToken);

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.body.token").value(mockToken));

            // makeJwt 호출 여부 확인
            mockStatic.verify(() -> makeJwt(request.userName()), times(1));
            verify(userService).validateUser(request.userName(), request.password());
        }
    }

    @Test
    void login_ShouldReturnNotFound_WhenCredentialsAreInvalid() throws Exception {
        AuthRequest request = new AuthRequest("invalidUser", "wrongPassword");

        // UserService의 validateUser가 빈 Optional을 반환하도록 설정
        when(userService.validateUser(request.userName(), request.password())).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())  // 상태 코드 404 검증
                .andExpect(jsonPath("$.code").value("D-02")) // ApiResponse의 'code' 필드 검증
                .andExpect(jsonPath("$.message").value("존재하지 않는 데이터입니다.")); // ApiResponse의 'message' 필드 검증

        verify(userService).validateUser(request.userName(), request.password());
    }

    @Test
    void protectedRoute_ShouldReturnOkStatus_WhenTokenIsValid() throws Exception {
        String mockToken = "Bearer mocked.jwt.token";

        try (MockedStatic<AuthUtil> mockStatic = mockStatic(AuthUtil.class)) {
            mockStatic.when(() -> validateJwt(mockToken)).thenReturn(true);

            mockMvc.perform(get("/auth/protected")
                            .header("Authorization", mockToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.body").value(String.valueOf(HttpStatus.OK)));

            // validateJwt 호출 여부 확인
            mockStatic.verify(() -> validateJwt(mockToken), times(1));
        }
    }

    @Test
    void protectedRoute_ShouldReturnUnauthorizedMessage_WhenTokenIsInvalid() throws Exception {
        String invalidToken = "Bearer invalid.jwt.token";

        try (MockedStatic<AuthUtil> mockStatic = mockStatic(AuthUtil.class)) {
            mockStatic.when(() -> validateJwt(invalidToken)).thenReturn(false);

            mockMvc.perform(get("/auth/protected")
                            .header("Authorization", invalidToken))
                    .andExpect(status().isOk()) // 상태 코드는 OK로 확인
                    .andExpect(jsonPath("$.code").value(String.valueOf(HttpStatus.UNAUTHORIZED.value()) + " UNAUTHORIZED")) // 'code' 필드에 '401' 확인
                    .andExpect(jsonPath("$.message").value("unauthorized")); // 'message' 필드에 'unauthorized' 확인
        }
    }
}