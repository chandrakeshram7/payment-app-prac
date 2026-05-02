package com.cantech.bank_service.exception;

public class BankAccountNotFound extends RuntimeException{
    public BankAccountNotFound(String message){
        super(message);
    }
}
