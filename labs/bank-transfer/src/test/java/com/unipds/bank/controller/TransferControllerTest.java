package com.unipds.bank.controller;

import com.unipds.bank.dto.TransferDTO;
import com.unipds.bank.exception.InvalidAccountException;
import com.unipds.bank.exception.InvalidBalanceException;
import com.unipds.bank.model.Transaction;
import com.unipds.bank.service.TransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransferController.class)
public class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransferService service;

    @Test
    public void deveRetornar200QuandoTransferenciaForSucesso() throws Exception {
        TransferDTO dto = new TransferDTO(1, 2, 50.0);

        Transaction mockTransaction = new Transaction();
        mockTransaction.setId(1001);
        mockTransaction.setAmount(50.0);
        mockTransaction.setTimestamp(LocalDateTime.now());

        when(service.transferValue(any(TransferDTO.class))).thenReturn(mockTransaction);

        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk()) // Espera 200 OK
                .andExpect(jsonPath("$.id").value(1001))
                .andExpect(jsonPath("$.amount").value(50.0));
    }

    @Test
    public void deveRetornar400QuandoSaldoForInsuficiente() throws Exception {
        TransferDTO dto = new TransferDTO(1, 2, 5000.0);

        // Simulando o lançamento da exceção pelo serviço
        when(service.transferValue(any(TransferDTO.class)))
                .thenThrow(new InvalidBalanceException("Saldo insuficiente para a transação."));

        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest()) // Espera 400 Bad Request (Graças ao @ControllerAdvice)
                .andExpect(jsonPath("$.error").value("Saldo insuficiente para a transação."));
    }

    @Test
    public void deveRetornar404QuandoContaNaoExistir() throws Exception {
        TransferDTO dto = new TransferDTO(99, 2, 10.0);

        // Simulando conta inexistente
        when(service.transferValue(any(TransferDTO.class)))
                .thenThrow(new InvalidAccountException("Account 99 does not exist"));

        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound()) // Espera 404 Not Found
                .andExpect(jsonPath("$.error").value("Account 99 does not exist"));
    }
}