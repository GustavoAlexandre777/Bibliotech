package br.com.bibliotech.diadema.dao;

import br.com.bibliotech.diadema.model.Categorias;
import br.com.bibliotech.diadema.model.Livros;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ILivrosDao extends CrudRepository<Livros, Long> {
    public Optional<Livros> findByIsbn(String isbn);


}
