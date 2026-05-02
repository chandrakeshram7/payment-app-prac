package com.cantech.bank_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class TransferRequestDTO {

    private String sender;

    private String reciever;

    private Double amount;

}
