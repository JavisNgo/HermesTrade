package com.ducnt.transfer.clients;

import com.ducnt.transfer.dto.response.AccountProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "accountBalance", url = "${clients.account.url}")
public interface AccountClient {
    @GetMapping("/account/{clientId}")
    ResponseEntity<AccountProfileResponse> validateAccount(@PathVariable(name = "clientId") String clientId);
}
