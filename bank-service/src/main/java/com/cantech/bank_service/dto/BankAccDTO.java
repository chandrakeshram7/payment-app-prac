package com.cantech.bank_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccDTO {

    private Long id;

    private String name;

    private Double balance;

    private String bankName;

    private String bankAccountType;

    private String aadharNo;

    private String kycStatus;

    private String accountNo;
}
