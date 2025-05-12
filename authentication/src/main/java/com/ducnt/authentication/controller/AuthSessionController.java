package com.ducnt.authentication.controller;

import com.ducnt.authentication.dto.request.LoginRequest;
import com.ducnt.authentication.service.AuthSessionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthSessionController {
    AuthSessionService authSessionService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return authSessionService.createAuthSession(loginRequest);
    }
}
