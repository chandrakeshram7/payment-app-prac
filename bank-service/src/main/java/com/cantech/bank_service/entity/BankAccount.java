package com.cantech.bank_service.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double balance;

    private String bankName;

    private String bankAccountType;

    @Column(unique = true)
    private String aadharNo;

    private String kycStatus;

    private String accountNo;
}
