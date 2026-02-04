package br.com.bibliotech.diadema.dto.response;



public record AutoresResponse(
        Long id,
        String nome,
        String nacionalidade
){
    public AutoresResponse(br.com.bibliotech.diadema.model.Autores aut){
        this(aut.getId(), aut.getNome(), aut.getNacionalidade());
    }
}
