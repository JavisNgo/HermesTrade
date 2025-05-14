package com.ducnt.authentication.clients;

import com.ducnt.authentication.dto.request.LoginRequest;
import com.ducnt.authentication.dto.response.ValidationAccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account", url = "${clients.account.url}")
public interface AccountClient {
    @PostMapping("/api/v1/internal/validate")
    ResponseEntity<ValidationAccountResponse> validateAccount(@RequestBody LoginRequest request);
}
