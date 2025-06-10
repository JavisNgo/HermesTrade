package com.ducnt.distributedratelimit.controller;

import com.ducnt.distributedratelimit.dto.request.RateLimitRequest;
import com.ducnt.distributedratelimit.dto.response.ApiResponse;
import com.ducnt.distributedratelimit.service.IRateLimitService;
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

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1")
public class RateLimitController {
    IRateLimitService rateLimitService;

    @PostMapping("/rate-limit")
    public ResponseEntity<ApiResponse> handleRateLimit(@RequestBody @Valid RateLimitRequest request) {
        boolean result = rateLimitService.handelRateLimit(request);
        if (result) {
            return new ResponseEntity<>(ApiResponse.builder().message("Pass rate-limit").build(), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(ApiResponse.builder().message("Rate limit exceeded").build(), HttpStatus.BAD_REQUEST);
        }
    }
}
