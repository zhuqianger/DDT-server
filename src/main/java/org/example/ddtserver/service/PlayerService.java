package org.example.ddtserver.service;

import org.example.ddtserver.dto.PlayerInfoDto;
import org.example.ddtserver.entity.Player;
import org.example.ddtserver.mapper.PlayerMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PlayerService {

    private final PlayerMapper playerMapper;
    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    public PlayerService(PlayerMapper playerMapper) {
        this.playerMapper = playerMapper;
    }

    public Player loginAndGetPlayer(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) return null;
        Player player = playerMapper.selectByUsername(username.trim());
        if (player == null) return null;
        if (player.getStatus() != null && player.getStatus() == 0) return null;
        if (!matchesPassword(password, player.getPassword())) return null;

        playerMapper.updateLastLoginTime(player.getId(), LocalDateTime.now());
        return player;
    }

    public PlayerInfoDto getPlayerInfo(long playerId) {
        Player p = playerMapper.selectById(playerId);
        if (p == null) return null;
        return new PlayerInfoDto(
                p.getId() == null ? 0L : p.getId(),
                p.getUsername(),
                p.getNickname(),
                p.getAvatar(),
                p.getLevel() == null ? 1 : p.getLevel(),
                p.getExp() == null ? 0L : p.getExp(),
                p.getGold() == null ? 0L : p.getGold(),
                p.getDiamond() == null ? 0L : p.getDiamond(),
                p.getStatus() == null ? 1 : p.getStatus(),
                p.getLastLoginTime(),
                p.getCreateTime(),
                p.getUpdateTime()
        );
    }

    private boolean matchesPassword(String raw, String stored) {
        if (stored == null) return false;
        // 兼容：若历史数据未加密，则按明文对比；若为 bcrypt（$2...），按 bcrypt 校验
        if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
            return bcrypt.matches(raw, stored);
        }
        return stored.equals(raw);
    }
}

