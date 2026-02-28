package org.example.ddtserver.dto;

/**
 * 每日签到信息：当前周期累计天数、今日是否已签、最后签到日期（yyyy-MM-dd）。
 */
public record DailySignInfoDto(
        int signInDays,
        boolean signedToday,
        String lastSignInDate
) {
}
