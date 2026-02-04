package br.com.bibliotech.diadema.dto.response;

import java.util.List;

public record VincularAutorResponse(
        String titulo,
        List<String> nomeAutores
) {}
