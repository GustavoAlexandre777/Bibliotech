package br.com.bibliotech.diadema.dto.response;

public record UsuarioResponse (
        String nome,
        String email,
        Long id
)
{
    public UsuarioResponse(br.com.bibliotech.diadema.model.Usuario usu){
         this(usu.getNome(), usu.getEmail(), usu.getId());
    }
}
