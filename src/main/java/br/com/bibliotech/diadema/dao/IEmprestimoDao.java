package br.com.bibliotech.diadema.dao;

import br.com.bibliotech.diadema.model.Emprestimos;
import br.com.bibliotech.diadema.model.Livros;
import br.com.bibliotech.diadema.model.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IEmprestimoDao extends CrudRepository<Emprestimos, Long> {

    public Optional<Emprestimos> findByCpfUsuarioCpf(String cpf);

    public Optional<Emprestimos> findByIsbn(Livros isbn);

    public Optional<Emprestimos> findBycpfUsuario(Usuario cpf);

    public Optional<Emprestimos> findByIsbnIsbn(String isbn);
}
