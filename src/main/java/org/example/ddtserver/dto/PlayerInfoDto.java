package org.example.ddtserver.dto;

import java.time.LocalDateTime;

public record PlayerInfoDto(
        long id,
        String username,
        String nickname,
        String avatar,
        int level,
        long exp,
        long gold,
        long diamond,
        int status,
        LocalDateTime lastLoginTime,
        LocalDateTime createTime,
        LocalDateTime updateTime
) {
}

