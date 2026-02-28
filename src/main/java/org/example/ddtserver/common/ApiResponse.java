package org.example.ddtserver.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse(int code, String msg, String data) {
    public static ApiResponse ok(String data) {
        return new ApiResponse(0, "ok", data);
    }

    public static ApiResponse fail(int code, String msg) {
        return new ApiResponse(code, msg, null);
    }
}

