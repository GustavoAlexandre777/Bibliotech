package br.com.bibliotech.diadema.dto.request;

import br.com.bibliotech.diadema.model.Livros;
import br.com.bibliotech.diadema.model.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmprestimoRequest (
        Usuario cpfUsuario,
        Livros isbn
){}
