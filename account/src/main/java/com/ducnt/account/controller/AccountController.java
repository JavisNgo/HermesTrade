package com.ducnt.account.controller;

import com.ducnt.account.dto.request.auth.UserRegistrationRequest;
import com.ducnt.account.dto.response.auth.AccountProfileResponse;
import com.ducnt.account.dto.response.auth.UserCreationResponse;
import com.ducnt.account.service.IAccountService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/api/v1")
public class AccountController {
    IAccountService accountService;

    @PostMapping("/active")
    public ResponseEntity<UserCreationResponse> activeAccount(@RequestBody @Valid UserRegistrationRequest request) {
        UserCreationResponse userCreationResponse = accountService.activateUser(request);
        return new ResponseEntity<>(userCreationResponse, HttpStatus.ACCEPTED);
    }

    @GetMapping("/account/{clientId}")
    public ResponseEntity<AccountProfileResponse> getAccount(@PathVariable(name = "clientId") String clientId) {
        AccountProfileResponse accountProfile = accountService.getAccountProfile(clientId);
        return new ResponseEntity<>(accountProfile, HttpStatus.OK);
    }
}
