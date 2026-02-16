package com.unipds.bank.service;

import com.unipds.bank.dto.TransferDTO;
import com.unipds.bank.exception.InvalidAccountException;
import com.unipds.bank.model.Account;
import com.unipds.bank.model.Transaction;
import com.unipds.bank.repository.AccountRepo;
import com.unipds.bank.repository.TransactionRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransferService {

    private final AccountRepo accountRepo;
    private final TransactionRepo transactionRepo;

    public TransferService(AccountRepo accountRepo, TransactionRepo transactionRepo) {
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }

    // A ANOTAÇÃO MAIS IMPORTANTE DESTA AULA:
    // Garante que, se ocorrer qualquer erro (ex: saldo negativo), TUDO é desfeito (Rollback).
    @Transactional
    public Transaction transferValue(TransferDTO dto) {
        // 1. Busca as contas (Lança erro se não existirem)
        Account src = accountRepo.findById(dto.debitAccountNumber())
                .orElseThrow(() -> new InvalidAccountException("Account " + dto.debitAccountNumber() + " does not exist"));

        Account dest = accountRepo.findById(dto.creditAccountNumber())
                .orElseThrow(() -> new InvalidAccountException("Account " + dto.creditAccountNumber() + " does not exist"));

        // 2. Credita na conta destino (Operação feita propositalmente primeiro para demonstrar o Rollback caso o próximo passo falhe)
        dest.setBalance(dest.getBalance() + dto.amount());
        accountRepo.save(dest);

        // 3. Debita da conta origem (Se o saldo for insuficiente, lança InvalidBalanceException AQUI e desfaz a etapa 2)
        src.setBalance(src.getBalance() - dto.amount());
        accountRepo.save(src);

        // 4. Registra a transação
        Transaction transaction = new Transaction();
        transaction.setDebitAccount(src);
        transaction.setCreditAccount(dest);
        transaction.setAmount(dto.amount());
        transaction.setTimestamp(LocalDateTime.now());

        return transactionRepo.save(transaction);
    }
}