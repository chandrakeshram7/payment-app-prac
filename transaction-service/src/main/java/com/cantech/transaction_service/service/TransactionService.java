package com.cantech.transaction_service.service;

import com.cantech.transaction_service.dto.TransferRequestDTO;
import com.cantech.transaction_service.entity.TransactionRecord;
import com.cantech.transaction_service.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void record(TransferRequestDTO transferRequestDTO) {
        TransactionRecord transactionRecord = modelMapper.map(transferRequestDTO, TransactionRecord.class);
        transactionRepository.save(transactionRecord);
    }
}
