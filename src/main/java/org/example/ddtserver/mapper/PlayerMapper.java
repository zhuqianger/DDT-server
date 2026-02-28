package org.example.ddtserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.ddtserver.entity.Player;

import java.time.LocalDateTime;

@Mapper
public interface PlayerMapper {

    Player selectByUsername(@Param("username") String username);

    Player selectById(@Param("id") long id);

    int updateLastLoginTime(@Param("id") long id, @Param("time") LocalDateTime time);
}
