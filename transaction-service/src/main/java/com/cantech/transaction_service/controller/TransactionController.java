package com.cantech.transaction_service.controller;

import com.cantech.transaction_service.client.BankFeignClient;
import com.cantech.transaction_service.dto.TransferRequestDTO;
import com.cantech.transaction_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
//    private final DiscoveryClient discoveryClient;
//    private final RestClient restClient;
    private final BankFeignClient bankFeignClient;
    private final TransactionService transactionService;

    @GetMapping("/helloFromBank")
    public String helloFromBankService(){
        return bankFeignClient.helloFromBank();
    }

    @PostMapping("/record")
    public void recordTransaction(@RequestBody TransferRequestDTO transferRequestDTO){
        transactionService.record(transferRequestDTO) ;
    }
}
