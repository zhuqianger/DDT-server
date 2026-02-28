package org.example.ddtserver.controller;

import org.example.ddtserver.common.ApiResponse;
import org.example.ddtserver.dto.LoginRequest;
import org.example.ddtserver.entity.Player;
import org.example.ddtserver.service.AuthTokenService;
import org.example.ddtserver.service.PlayerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final PlayerService playerService;
    private final AuthTokenService authTokenService;

    public AuthController(PlayerService playerService, AuthTokenService authTokenService) {
        this.playerService = playerService;
        this.authTokenService = authTokenService;
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest req) {
        if (req == null) return ApiResponse.fail(400, "请求体为空");
        Player player = playerService.loginAndGetPlayer(req.username(), req.password());
        if (player == null) return ApiResponse.fail(401, "账号或密码错误，或账号被禁用");
        String token = authTokenService.issueToken(player.getId());
        return ApiResponse.ok(token);
    }
}

