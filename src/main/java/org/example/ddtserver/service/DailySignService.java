package org.example.ddtserver.service;

import org.example.ddtserver.dto.DailySignInfoDto;
import org.example.ddtserver.entity.DailyLoginRecord;
import org.example.ddtserver.mapper.DailyLoginRecordMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DailySignService {

    private final DailyLoginRecordMapper dailyLoginRecordMapper;

    public DailySignService(DailyLoginRecordMapper dailyLoginRecordMapper) {
        this.dailyLoginRecordMapper = dailyLoginRecordMapper;
    }

    /**
     * 获取当前玩家的签到状态（累计天数、今日是否已签、最后签到日期）。
     */
    public DailySignInfoDto getInfo(long playerId) {
        DailyLoginRecord record = dailyLoginRecordMapper.selectByPlayerId(playerId);
        LocalDate today = LocalDate.now();

        if (record == null) {
            return new DailySignInfoDto(0, false, null);
        }

        boolean signedToday = today.equals(record.getLastSignInDate());
        int days = record.getSignInDays() == null ? 0 : record.getSignInDays();
        String lastDateStr = record.getLastSignInDate() == null
                ? null
                : record.getLastSignInDate().toString();

        return new DailySignInfoDto(days, signedToday, lastDateStr);
    }

    /**
     * 执行签到。跨月则重置为 1；今日已签返回 null（调用方用 409 处理）。
     */
    public DailySignInfoDto sign(long playerId) {
        LocalDate today = LocalDate.now();
        DailyLoginRecord record = dailyLoginRecordMapper.selectByPlayerId(playerId);

        if (record == null) {
            record = new DailyLoginRecord();
            record.setPlayerId(playerId);
            record.setSignInDays(1);
            record.setLastSignInDate(today);
            dailyLoginRecordMapper.insert(record);
            return new DailySignInfoDto(1, true, today.toString());
        }

        LocalDate lastDate = record.getLastSignInDate();
        if (today.equals(lastDate)) {
            return null;
        }

        int days = record.getSignInDays() == null ? 0 : record.getSignInDays();
        if (lastDate == null
                || lastDate.getYear() != today.getYear()
                || lastDate.getMonthValue() != today.getMonthValue()) {
            days = 0;
        }
        days += 1;

        dailyLoginRecordMapper.updateOnSignIn(playerId, days, today);
        return new DailySignInfoDto(days, true, today.toString());
    }
}
