package org.zeroskill.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public record PostDto(
        Long id,
        String title,
        String content,
        @JsonIgnore
        UserDto userDto,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
