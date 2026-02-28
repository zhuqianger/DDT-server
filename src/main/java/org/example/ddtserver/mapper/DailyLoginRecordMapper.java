package org.example.ddtserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.ddtserver.entity.DailyLoginRecord;

import java.time.LocalDate;

@Mapper
public interface DailyLoginRecordMapper {

    DailyLoginRecord selectByPlayerId(@Param("playerId") long playerId);

    int insert(DailyLoginRecord record);

    int updateOnSignIn(@Param("playerId") long playerId,
                       @Param("signInDays") int signInDays,
                       @Param("lastSignInDate") LocalDate lastSignInDate);
}
