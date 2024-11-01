package org.zeroskill.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.zeroskill.common.dto.response.ApiResponse;

@FeignClient(name = "auth-client", url = "${auth-api.url}") // auth-api의 URL을 application.yml에 정의
public interface AuthClient {
    @GetMapping("/auth/protected")
    ApiResponse<?> validateToken(@RequestHeader("Authorization") String token);
}
