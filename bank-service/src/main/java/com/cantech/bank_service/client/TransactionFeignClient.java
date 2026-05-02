package com.cantech.bank_service.client;

import com.cantech.bank_service.dto.TransferRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "transaction-service", path = "/transactions", url="${TRANSACTION_SERVICE_URL:}")
public interface TransactionFeignClient {

    @PostMapping("/record")
    void recordTransaction(TransferRequestDTO transferRequestDTO);
}
