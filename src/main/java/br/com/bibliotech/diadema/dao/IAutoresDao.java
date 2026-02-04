package br.com.bibliotech.diadema.dao;

import br.com.bibliotech.diadema.model.Autores;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IAutoresDao extends CrudRepository<Autores, Long> {

}
