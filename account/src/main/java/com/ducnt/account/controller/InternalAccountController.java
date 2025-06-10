package com.ducnt.account.controller;

import com.ducnt.account.dto.request.LoginRequest;
import com.ducnt.account.dto.response.ValidationAccountResponse;
import com.ducnt.account.service.IAccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/api/v1")
public class InternalAccountController {
    IAccountService activationService;

    @PostMapping("/internal/validate")
    public ResponseEntity<ValidationAccountResponse> validateAccount(@RequestBody LoginRequest request) {
        ValidationAccountResponse validationAccountResponse = activationService.validateAccount(request);
        return new ResponseEntity<>(validationAccountResponse, HttpStatus.OK);
    }
}
