package com.unipds.bank.model;

import com.unipds.bank.exception.InvalidBalanceException;
import jakarta.persistence.*;

@Entity
@Table(name = "tbl_account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer number; // ID da conta

    @Column(nullable = false)
    private Double balance; // Saldo

    // Getters
    public Integer getNumber() { return number; }
    public Double getBalance() { return balance; }

    // Regra de Negócio protegendo a consistência do saldo
    public void setBalance(Double balance) {
        if (balance < 0) {
            throw new InvalidBalanceException("Saldo insuficiente para a transação.");
        }
        this.balance = balance;
    }

    // Método auxiliar (não é estritamente obrigatório, mas facilita testes manuais via DB)
    public void setNumber(Integer number) { this.number = number; }
}