package com.ducnt.account.controller;

import com.ducnt.account.dto.request.UserRegistrationRequest;
import com.ducnt.account.dto.response.ApiResponse;
import com.ducnt.account.dto.response.UserCreationResponse;
import com.ducnt.account.service.IAccountService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/api/v1")
public class AccountController {
    IAccountService activationService;

    @PostMapping("/active")
    public ResponseEntity<UserCreationResponse> activeAccount(@RequestBody @Valid UserRegistrationRequest request) {
        UserCreationResponse userCreationResponse = activationService.activateUser(request);
        return new ResponseEntity<>(userCreationResponse, HttpStatus.ACCEPTED);
    }
}
