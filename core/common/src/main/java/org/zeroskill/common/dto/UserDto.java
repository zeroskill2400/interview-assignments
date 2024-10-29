package org.zeroskill.common.dto;

import org.zeroskill.common.entity.Gender;

import java.time.LocalDateTime;

public record UserDto(
        Long id,
        String username,
        String email,
        String password,
        Integer age,
        String phone,
        Gender gender,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static UserDto hashPassword(UserDto dto, String hashedPassword) {
        return new UserDto(dto.id, dto.username, dto.email, hashedPassword, dto.age, dto.phone, dto.gender, null, null);
    }
}

