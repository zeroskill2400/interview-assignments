package org.zeroskill.authapi.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.zeroskill.authapi.dto.request.AuthRequest;
import org.zeroskill.authapi.dto.response.AuthResponse;
import org.zeroskill.common.dto.response.ApiResponse;
import org.zeroskill.common.exception.ErrorType;
import org.zeroskill.common.exception.InterviewException;
import org.zeroskill.common.service.UserService;

import static org.zeroskill.common.util.AuthUtil.makeJwt;
import static org.zeroskill.common.util.AuthUtil.validateJwt;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    private final UserService userService;

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody AuthRequest request) {
        request.check();
        String username = request.userName();
        String password = request.password();

        // UserService를 통해 사용자 이름과 비밀번호 검증
        return userService.validateUser(username, password)
                .map(validUser -> {
                    // JWT 토큰 생성
                    String token = makeJwt(username);
                    return ApiResponse.of(new AuthResponse(token));
                })
                .orElseThrow(() -> new InterviewException(ErrorType.DATA_NOT_FOUND, logger::error));
    }

    @GetMapping("/protected")
    public ApiResponse<?> protectedRoute(@RequestHeader("Authorization") String token) {
        if(validateJwt(token)) {
            return ApiResponse.of("success granted");
        }
        return ApiResponse.of(String.valueOf(HttpStatus.UNAUTHORIZED), "unauthorized");
    }
}
