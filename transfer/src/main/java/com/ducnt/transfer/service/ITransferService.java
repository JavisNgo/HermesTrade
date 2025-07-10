package com.ducnt.transfer.service;

import com.ducnt.transfer.dto.request.TradeRequest;
import com.ducnt.transfer.dto.response.TransferResponse;

public interface ITransferService {
    TransferResponse initiateP2PTransaction(TradeRequest tradeRequest, String idempotencyKey);

}
