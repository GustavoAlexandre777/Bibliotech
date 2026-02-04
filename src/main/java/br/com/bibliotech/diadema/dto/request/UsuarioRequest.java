package br.com.bibliotech.diadema.dto.request;

public record UsuarioRequest (
    String nome,
    String email,
    String cpf
){}
