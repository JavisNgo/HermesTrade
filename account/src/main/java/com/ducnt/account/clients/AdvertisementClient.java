package com.ducnt.account.clients;

import com.ducnt.account.dto.request.binance.AdvertisementApiRequest;
import com.ducnt.account.dto.response.binance.AdvertisementApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "binance", url = "https://p2p.binance.com")
public interface AdvertisementClient {
    @PostMapping("/bapi/c2c/v2/friendly/c2c/adv/search")
    ResponseEntity<AdvertisementApiResponse> getAdvertisement(@RequestBody AdvertisementApiRequest request);
}
