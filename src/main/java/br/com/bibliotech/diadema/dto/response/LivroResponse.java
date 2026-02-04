package br.com.bibliotech.diadema.dto.response;

public record LivroResponse(
        Long id,
        String titulo
){
    public LivroResponse(br.com.bibliotech.diadema.model.Livros liv){
        this(liv.getId(), liv.getTitulo());
    }
}
