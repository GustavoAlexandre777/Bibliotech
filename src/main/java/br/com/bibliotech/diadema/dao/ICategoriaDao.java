package br.com.bibliotech.diadema.dao;

import br.com.bibliotech.diadema.model.Categorias;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ICategoriaDao extends CrudRepository<Categorias, Long> {
    public Optional<Categorias> findByNome(String nome);
}
