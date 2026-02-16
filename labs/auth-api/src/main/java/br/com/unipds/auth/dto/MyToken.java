package br.com.unipds.auth.dto;

// Usamos Record para criar um DTO simples e imutável que retorna o token
public record MyToken(String token) {}