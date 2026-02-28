package org.example.ddtserver.config;

import org.example.ddtserver.websocket.AuthHandshakeInterceptor;
import org.example.ddtserver.websocket.GameWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final AuthHandshakeInterceptor authHandshakeInterceptor;
    private final GameWebSocketHandler gameWebSocketHandler;

    public WebSocketConfig(AuthHandshakeInterceptor authHandshakeInterceptor, GameWebSocketHandler gameWebSocketHandler) {
        this.authHandshakeInterceptor = authHandshakeInterceptor;
        this.gameWebSocketHandler = gameWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(gameWebSocketHandler, "/ws")
                .addInterceptors(authHandshakeInterceptor)
                .setAllowedOrigins("*");
    }
}
