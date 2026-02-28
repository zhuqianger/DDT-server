package org.example.ddtserver.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ddtserver.dto.PlayerInfoDto;
import org.example.ddtserver.dto.WsEnvelope;
import org.example.ddtserver.service.PlayerService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    private static final String CMD_PLAYER_GET = "player.get";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PlayerService playerService;

    public GameWebSocketHandler(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long playerId = (Long) session.getAttributes().get(AuthHandshakeInterceptor.ATTR_PLAYER_ID);
        if (playerId != null) {
            session.getAttributes().put("playerId", playerId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long playerId = (Long) session.getAttributes().get(AuthHandshakeInterceptor.ATTR_PLAYER_ID);
        if (playerId == null) {
            sendEnvelope(session, WsEnvelope.fail("", null, 401, "未认证"));
            return;
        }

        String payload = message.getPayload();
        if (payload == null || payload.isBlank()) {
            sendEnvelope(session, WsEnvelope.fail("", null, 400, "空消息"));
            return;
        }

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> raw = objectMapper.readValue(payload, Map.class);
            String cmd = (String) raw.get("cmd");
            String reqId = (String) raw.get("reqId");

            if (CMD_PLAYER_GET.equals(cmd)) {
                PlayerInfoDto info = playerService.getPlayerInfo(playerId);
                String data = info == null ? "{}" : objectMapper.writeValueAsString(info);
                sendEnvelope(session, WsEnvelope.ok(CMD_PLAYER_GET, reqId, data));
                return;
            }

            sendEnvelope(session, WsEnvelope.fail(cmd != null ? cmd : "", reqId, 404, "未知命令"));
        } catch (Exception e) {
            sendEnvelope(session, WsEnvelope.fail("", null, 400, "消息格式错误"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    }

    private void sendEnvelope(WebSocketSession session, WsEnvelope envelope) {
        try {
            String json = objectMapper.writeValueAsString(envelope);
            session.sendMessage(new TextMessage(json));
        } catch (IOException ignored) {
        }
    }
}
