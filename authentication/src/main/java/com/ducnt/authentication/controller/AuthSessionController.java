package com.ducnt.authentication.controller;

import com.ducnt.authentication.dto.request.LoginRequest;
import com.ducnt.authentication.dto.response.ValidationAccountResponse;
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
    public ResponseEntity<ValidationAccountResponse> login(@RequestBody LoginRequest loginRequest) {
        ValidationAccountResponse response = authSessionService.authenticate(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
