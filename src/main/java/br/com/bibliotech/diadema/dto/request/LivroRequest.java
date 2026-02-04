package br.com.bibliotech.diadema.dto.request;

import br.com.bibliotech.diadema.model.Categorias;

import java.time.LocalDate;

public record LivroRequest(
        String titulo,
        String isbn,
        LocalDate dataPublicacao,
        Categorias idCategoria

){}
