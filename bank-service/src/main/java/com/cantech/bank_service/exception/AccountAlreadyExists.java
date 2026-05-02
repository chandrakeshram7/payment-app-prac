package com.cantech.bank_service.exception;

public class AccountAlreadyExists extends RuntimeException{
    public AccountAlreadyExists(String message){
        super(message);
    }
}
