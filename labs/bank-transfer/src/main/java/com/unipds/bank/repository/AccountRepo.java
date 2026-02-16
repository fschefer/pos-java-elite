package com.unipds.bank.repository;

import com.unipds.bank.model.Account;
import org.springframework.data.repository.ListCrudRepository;

public interface AccountRepo extends ListCrudRepository<Account, Integer> {}