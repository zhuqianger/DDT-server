package org.example.ddtserver.websocket;

import jakarta.servlet.http.HttpServletRequest;
import org.example.ddtserver.service.AuthTokenService;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;

@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    public static final String ATTR_PLAYER_ID = "playerId";

    private final AuthTokenService authTokenService;

    public AuthHandshakeInterceptor(AuthTokenService authTokenService) {
        this.authTokenService = authTokenService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String token = null;

        // 1) Query param: ?token=xxx
        URI uri = request.getURI();
        if (uri != null && uri.getQuery() != null) {
            for (String kv : uri.getQuery().split("&")) {
                int idx = kv.indexOf('=');
                if (idx > 0) {
                    String k = kv.substring(0, idx);
                    String v = kv.substring(idx + 1);
                    if ("token".equalsIgnoreCase(k)) {
                        token = v;
                        break;
                    }
                }
            }
        }

        // 2) Authorization header: Bearer xxx
        if (token == null) {
            String auth = request.getHeaders().getFirst("Authorization");
            if (auth != null && auth.toLowerCase().startsWith("bearer ")) {
                token = auth.substring("bearer ".length()).trim();
            }
        }

        // 3) Fallback: from HTTP request attribute
        if (token == null && request instanceof ServletServerHttpRequest servletReq) {
            HttpServletRequest raw = servletReq.getServletRequest();
            String t = raw.getParameter("token");
            if (t != null && !t.isBlank()) token = t;
        }

        Long playerId = authTokenService.verifyAndGetPlayerId(token);
        if (playerId == null) return false;

        attributes.put(ATTR_PLAYER_ID, playerId);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}

