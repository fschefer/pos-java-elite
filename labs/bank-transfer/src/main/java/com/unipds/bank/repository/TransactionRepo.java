package com.unipds.bank.repository;

import com.unipds.bank.model.Transaction;
import org.springframework.data.repository.ListCrudRepository;

public interface TransactionRepo extends ListCrudRepository<Transaction, Integer> {}