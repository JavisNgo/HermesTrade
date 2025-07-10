package com.ducnt.account.dto.request.binance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdvertisementApiRequest {
    private String fiat = "VND";
    private Integer page = 1;
    private Integer rows = 10;
    private String tradeType = "BUY";
    private String asset = "USDT";
    private Boolean proMerchantAds = false;
    private Boolean shieldMerchantAds = false;
    private String filterType = "tradable";
    private String publisherType = "merchant";

    private Boolean tradedWith = false;
    private Boolean followed = false;
}
