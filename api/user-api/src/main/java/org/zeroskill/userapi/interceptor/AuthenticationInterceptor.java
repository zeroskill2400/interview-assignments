package org.zeroskill.userapi.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.zeroskill.common.client.AuthClient;
import org.zeroskill.common.dto.response.ApiResponse;
import org.zeroskill.common.exception.ErrorType;
import org.zeroskill.common.exception.InterviewException;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final Logger logger = LogManager.getLogger(AuthenticationInterceptor.class);

    private final AuthClient authClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("POST".equalsIgnoreCase(request.getMethod()) && (request.getRequestURI().matches("^/users/\\d+$") || "/users".equals(request.getRequestURI()))) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token != null) {
            try {
                ApiResponse<?> authResponse = authClient.validateToken(token);
                // authResponse가 null이 아니고 상태 코드가 OK인 경우 인증 성공으로 간주
                if (authResponse != null && authResponse.body().equals(String.valueOf(HttpStatus.OK))) {
                    return true;
                }
            } catch (Exception e) {
                // 인증 요청 중 예외 발생 시, 로그 기록 및 인증 실패 처리
                throw new InterviewException(ErrorType.AUTHENTICATION_FAILED, logger::error);
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Unauthorized");
        return false; // 인증 실패 시 요청 중단
    }
}
