package com.unipds.bank.dto;

public record TransferDTO(Integer debitAccountNumber, Integer creditAccountNumber, Double amount) {}