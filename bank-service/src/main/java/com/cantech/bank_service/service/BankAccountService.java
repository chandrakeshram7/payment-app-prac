package com.cantech.bank_service.service;

import com.cantech.bank_service.client.TransactionFeignClient;
import com.cantech.bank_service.dto.BankAccDTO;
import com.cantech.bank_service.dto.GetAccountDTO;
import com.cantech.bank_service.dto.TransferRequestDTO;
import com.cantech.bank_service.entity.BankAccount;
import com.cantech.bank_service.exception.AccountAlreadyExists;
import com.cantech.bank_service.exception.BankAccountNotFound;
import com.cantech.bank_service.exception.TransactionFailureException;
import com.cantech.bank_service.repository.BankAccountRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankAccountService {

    private static long counter = 1000000000;

    private final ModelMapper modelMapper;
    private final BankAccountRepository bankAccountRepository;
    private final TransactionFeignClient transactionFeignClient;

    public String createAccount(BankAccDTO bankAccDTO){
        log.info("Start of creation of account");
        BankAccount bankAccount = bankAccountRepository.findByAadharNo(bankAccDTO.getAadharNo()).orElse(null) ;
        if(bankAccount != null){
            throw new AccountAlreadyExists("Account with Aadhar number already exists");
        }
        bankAccount =modelMapper.map(bankAccDTO, BankAccount.class);
        String accNo = "BOI"+ counter++;
        bankAccount.setAccountNo(accNo);
        bankAccountRepository.save(bankAccount);
        log.info("Creating the bank account with acc no {}",accNo);
        return bankAccount.getAccountNo();

    }

    public BankAccDTO getAccountByAccountNo(GetAccountDTO getAccountDTO){
        BankAccount bankAccount = bankAccountRepository.findByAccountNo(getAccountDTO.getAccountNo()).orElse(null);
        if(bankAccount == null){
            throw new BankAccountNotFound("Bank account with account no. "+getAccountDTO.getAccountNo()+" not found");
        }

        return modelMapper.map(bankAccount, BankAccDTO.class);
    }

    public String deleteAccount(GetAccountDTO getAccountDTO){
        BankAccount bankAccount = bankAccountRepository.findByAccountNo(getAccountDTO.getAccountNo()).orElse(null);
        if(bankAccount == null){
            throw new BankAccountNotFound("Bank account with account no. "+getAccountDTO.getAccountNo()+" not found");
        }

        bankAccountRepository.delete(bankAccount);
        return getAccountDTO.getAccountNo();
    }

    public String updateAccount(String accountNumber, BankAccDTO bankAccDTO){
        BankAccount bankAccount = bankAccountRepository.findByAccountNo(accountNumber).orElse(null);
        if(bankAccount == null){
            throw new BankAccountNotFound("Bank account with account no. "+accountNumber+" not found");
        }

        modelMapper.map(bankAccDTO, bankAccount);
        bankAccount.setAccountNo(accountNumber);
        return accountNumber;
    }

//    @Retry(name = "transactionRetry", fallbackMethod = "transferMoneyFallback")
//    @RateLimiter(name = "transactionRateLimiter", fallbackMethod = "transferMoneyFallback")
    @CircuitBreaker(name = "transactionCircuitBreaker", fallbackMethod = "transferMoneyFallback")
    public String transferMoney(TransferRequestDTO transferRequestDTO){
        String senderAccNo = transferRequestDTO.getSender();
        String receiverAccNo = transferRequestDTO.getReciever();

        BankAccount sender = bankAccountRepository.findByAccountNo(senderAccNo).orElse(null);
        if(sender == null){
            throw new TransactionFailureException("Sender details could not be fetched");
        }

        BankAccount receiver = bankAccountRepository.findByAccountNo(receiverAccNo).orElse(null);
        if(receiver == null){
            throw  new TransactionFailureException("Receiver details could not be fetched");
        }

        Double senderBalance = sender.getBalance();
        Double receiverBalance = receiver.getBalance();

        Double amount = transferRequestDTO.getAmount();

        if(amount > senderBalance){
            throw new TransactionFailureException("Insufficient funds, cannot proceed");
        }

        sender.setBalance(sender.getBalance() - amount);  //debit logic
        try{
            receiver.setBalance(receiver.getBalance() + amount);  //credit logic

            transactionFeignClient.recordTransaction(transferRequestDTO);  //record transaction
        }
        catch(TransactionFailureException ex){
            sender.setBalance(sender.getBalance() + amount);   //saga pattern
            log.info("Transaction has been rolled back");
        }

        return "Transaction Successful";

    }

    public String transferMoneyFallback(TransferRequestDTO transferRequestDTO, Throwable throwable){
        log.error("Fallback occurred due to : {} ", throwable.getMessage());
        return "Fallback";
    }









}
