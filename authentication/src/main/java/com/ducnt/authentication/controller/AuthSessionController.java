package com.ducnt.authentication.controller;

import com.ducnt.authentication.dto.request.LoginRequest;
import com.ducnt.authentication.dto.response.AuthSessionResponse;
import com.ducnt.authentication.service.AuthSessionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthSessionController {
    AuthSessionService authSessionService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthSessionResponse> login(@RequestBody LoginRequest loginRequest){
        AuthSessionResponse response = authSessionService.authenticate(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader("clientId") String clientId,
                                 @RequestHeader("sessionId") String sessionId) {
        authSessionService.logout(clientId, sessionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
