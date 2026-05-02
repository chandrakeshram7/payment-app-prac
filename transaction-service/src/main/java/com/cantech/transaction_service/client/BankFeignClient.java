package com.cantech.transaction_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "bank-service", path="/bank", url="${BANK_SERVICE_URL:}")
public interface BankFeignClient {

    @GetMapping("/hello")
    String helloFromBank();
}
