package br.com.bibliotech.diadema.dao;

import br.com.bibliotech.diadema.model.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {
    public Optional<Usuario> findByCpf(String cpf);

    public Optional<Usuario> deleteByCpf(String cpf);

    public Optional<Usuario> findByEmail(String email);
}
