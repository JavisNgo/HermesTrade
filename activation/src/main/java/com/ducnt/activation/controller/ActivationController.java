package com.ducnt.activation.controller;

import com.ducnt.activation.dto.request.UserRegistrationRequest;
import com.ducnt.activation.dto.response.ApiResponse;
import com.ducnt.activation.service.IActivationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/api/v1")
public class ActivationController {
    IActivationService activationService;

    @PostMapping("/active")
    public ApiResponse activeAccount(@RequestBody @Valid UserRegistrationRequest request) {
        boolean result = activationService.activateUser(request);
        return ApiResponse
                .builder()
                .code(result ? HttpStatus.OK.value() : HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(result ? "Your account is active successfully" : "Internal server error")
                .build();
    }
}
