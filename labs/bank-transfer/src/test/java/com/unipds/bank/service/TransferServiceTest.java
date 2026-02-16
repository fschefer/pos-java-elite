package com.unipds.bank.service;

import com.unipds.bank.dto.TransferDTO;
import com.unipds.bank.exception.InvalidAccountException;
import com.unipds.bank.exception.InvalidBalanceException;
import com.unipds.bank.model.Account;
import com.unipds.bank.model.Transaction;
import com.unipds.bank.repository.AccountRepo;
import com.unipds.bank.repository.TransactionRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @InjectMocks
    private TransferService service;

    @Mock
    private AccountRepo accountRepo;

    @Mock
    private TransactionRepo transactionRepo;

    private Account srcAccount;
    private Account destAccount;

    @BeforeEach
    void setUp() {
        srcAccount = new Account();
        srcAccount.setNumber(1);
        srcAccount.setBalance(100.0); // Conta origem tem 100

        destAccount = new Account();
        destAccount.setNumber(2);
        destAccount.setBalance(50.0); // Conta destino tem 50
    }

    @Test
    public void deveRealizarTransferenciaComSucesso() {
        // Cenário: Transferindo 40.0 da conta 1 para a conta 2
        TransferDTO dto = new TransferDTO(1, 2, 40.0);

        when(accountRepo.findById(1)).thenReturn(Optional.of(srcAccount));
        when(accountRepo.findById(2)).thenReturn(Optional.of(destAccount));
        when(transactionRepo.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        // Execução
        Transaction result = service.transferValue(dto);

        // Validação
        assertNotNull(result);
        assertEquals(40.0, result.getAmount());
        assertEquals(60.0, srcAccount.getBalance()); // 100 - 40 = 60
        assertEquals(90.0, destAccount.getBalance()); // 50 + 40 = 90
        verify(transactionRepo, times(1)).save(any(Transaction.class));
    }

    @Test
    public void deveLancarExcecaoEBloquearTransacaoPorSaldoInsuficiente() {
        // Cenário: Tentando transferir 200.0 (maior que o saldo da origem que é 100.0)
        TransferDTO dto = new TransferDTO(1, 2, 200.0);

        when(accountRepo.findById(1)).thenReturn(Optional.of(srcAccount));
        when(accountRepo.findById(2)).thenReturn(Optional.of(destAccount));

        // Execução e Validação: Espera-se que lance a InvalidBalanceException
        InvalidBalanceException exception = assertThrows(InvalidBalanceException.class, () -> {
            service.transferValue(dto);
        });

        assertEquals("Saldo insuficiente para a transação.", exception.getMessage());

        // Verifica que o método save da transação NUNCA foi chamado (simulando o Rollback)
        verify(transactionRepo, never()).save(any(Transaction.class));
    }

    @Test
    public void deveLancarExcecaoSeContaOrigemNaoExistir() {
        // Cenário: Conta de origem 99 não existe
        TransferDTO dto = new TransferDTO(99, 2, 10.0);

        when(accountRepo.findById(99)).thenReturn(Optional.empty());

        // Execução e Validação
        InvalidAccountException exception = assertThrows(InvalidAccountException.class, () -> {
            service.transferValue(dto);
        });

        assertEquals("Account 99 does not exist", exception.getMessage());
        verify(transactionRepo, never()).save(any(Transaction.class));
    }
}