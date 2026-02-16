package com.unipds.bank.controller;

import com.unipds.bank.dto.TransferDTO;
import com.unipds.bank.model.Transaction;
import com.unipds.bank.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService service;

    public TransferController(TransferService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Transaction> makeTransfer(@RequestBody TransferDTO dto) {
        Transaction transaction = service.transferValue(dto);
        return ResponseEntity.ok(transaction);
    }
}