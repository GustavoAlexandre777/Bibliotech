package br.com.bibliotech.diadema.dto.response;

public record CategoriaResponse(
        Long id,
        String nome
)
{
    public CategoriaResponse(br.com.bibliotech.diadema.model.Categorias cat){
        this(cat.getId(), cat.getNome());
    }
}
