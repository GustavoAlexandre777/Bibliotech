package br.com.bibliotech.diadema.dto.request;

import java.util.List;

public record AtualizarAutoresRequest(
        Long idLivro,
        List<Long> idsAutores
){}
