package com.unipds.bank.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "debit_account_id")
    private Account debitAccount;

    @ManyToOne
    @JoinColumn(name = "credit_account_id")
    private Account creditAccount;

    private Double amount;
    private LocalDateTime timestamp;

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Account getDebitAccount() { return debitAccount; }
    public void setDebitAccount(Account debitAccount) { this.debitAccount = debitAccount; }
    public Account getCreditAccount() { return creditAccount; }
    public void setCreditAccount(Account creditAccount) { this.creditAccount = creditAccount; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}