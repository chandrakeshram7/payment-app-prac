package com.cantech.bank_service.controller;

import com.cantech.bank_service.dto.BankAccDTO;
import com.cantech.bank_service.dto.GetAccountDTO;
import com.cantech.bank_service.dto.TransferRequestDTO;
import com.cantech.bank_service.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
@RefreshScope
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @Value("${my.variable}")
    private String variable;

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody BankAccDTO bankAccDTO){
        return new ResponseEntity<>(bankAccountService.createAccount(bankAccDTO), HttpStatus.CREATED);
    }

    @GetMapping("/getAccount")
    public ResponseEntity<?> getAccount(@RequestBody GetAccountDTO getAccountDTO){
        return new ResponseEntity<>(bankAccountService.getAccountByAccountNo(getAccountDTO), HttpStatus.OK);
    }

    @PutMapping("/updateAccount/{accountNumber}")
    public ResponseEntity<?> updateAccount(@PathVariable String accountNumber, @RequestBody BankAccDTO bankAccDTO){
        return new ResponseEntity<>(bankAccountService.updateAccount(accountNumber, bankAccDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<?> deleteAccount(@RequestBody GetAccountDTO getAccountDTO){
        return new ResponseEntity<>(bankAccountService.deleteAccount(getAccountDTO), HttpStatus.OK);
    }

    @GetMapping("/hello")
    public String helloFromBank(){
        return "Hello From Bank "+ variable;
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(@RequestBody TransferRequestDTO transferRequestDTO){
        return new ResponseEntity<>(bankAccountService.transferMoney(transferRequestDTO), HttpStatus.OK);
    }
}
