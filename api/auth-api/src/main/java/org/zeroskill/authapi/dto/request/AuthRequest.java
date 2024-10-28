package org.zeroskill.authapi.dto.request;

public record AuthRequest(
        String userName,
        String password
) {
}
