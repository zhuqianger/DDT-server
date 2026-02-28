package org.example.ddtserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record WsEnvelope(
        String cmd,
        String reqId,
        Integer code,
        String msg,
        String data
) {
    public static WsEnvelope ok(String cmd, String reqId, String data) {
        return new WsEnvelope(cmd, reqId, 0, "ok", data);
    }

    public static WsEnvelope fail(String cmd, String reqId, int code, String msg) {
        return new WsEnvelope(cmd, reqId, code, msg, null);
    }
}

