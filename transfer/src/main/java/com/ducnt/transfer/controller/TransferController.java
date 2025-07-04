package com.ducnt.transfer.controller;

import com.ducnt.transfer.dto.request.TradeRequest;
import com.ducnt.transfer.dto.response.TransferResponse;
import com.ducnt.transfer.service.ITransferService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransferController {
    ITransferService transferService;
    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(
            @RequestHeader(name = "idempotency-key") String idempotencyKey,
            @RequestBody TradeRequest tradeRequest
    ) {
        TransferResponse result = transferService.transfer(tradeRequest, idempotencyKey);
        return ResponseEntity.ok(result);
    }
}
