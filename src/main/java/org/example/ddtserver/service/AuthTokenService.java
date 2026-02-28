package org.example.ddtserver.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthTokenService {

    private static final Duration TTL = Duration.ofHours(12);

    private record TokenEntry(long playerId, Instant expiresAt) {
    }

    private final Map<String, TokenEntry> tokenToPlayer = new ConcurrentHashMap<>();

    public String issueToken(long playerId) {
        String token = UUID.randomUUID().toString().replace("-", "");
        tokenToPlayer.put(token, new TokenEntry(playerId, Instant.now().plus(TTL)));
        return token;
    }

    public Long verifyAndGetPlayerId(String token) {
        if (token == null || token.isBlank()) return null;
        TokenEntry entry = tokenToPlayer.get(token);
        if (entry == null) return null;
        if (Instant.now().isAfter(entry.expiresAt)) {
            tokenToPlayer.remove(token);
            return null;
        }
        return entry.playerId;
    }
}

