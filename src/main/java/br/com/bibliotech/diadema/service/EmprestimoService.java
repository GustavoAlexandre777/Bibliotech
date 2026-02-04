package br.com.bibliotech.diadema.service;

import br.com.bibliotech.diadema.Exceptions.*;
import br.com.bibliotech.diadema.dao.IEmprestimoDao;
import br.com.bibliotech.diadema.dao.ILivrosDao;
import br.com.bibliotech.diadema.dao.IUsuarioDao;
import br.com.bibliotech.diadema.dto.request.EmprestimoRequest;
import br.com.bibliotech.diadema.dto.response.EmprestimoResponse;
import br.com.bibliotech.diadema.model.Emprestimos;
import br.com.bibliotech.diadema.model.Livros;
import br.com.bibliotech.diadema.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EmprestimoService {

    private IEmprestimoDao dao;
    @Autowired
    private ILivrosDao daoLivro;
    @Autowired
    private IUsuarioDao daoUsu;

    public EmprestimoService(IEmprestimoDao dao) {
        super();
        this.dao = dao;
    }

    public EmprestimoResponse criar(EmprestimoRequest request) {


        if(request.cpfUsuario() == null || request.isbn() == null){
                throw new NullException("Impossível criar o empréstimo porque dados obrigatórios não foram inseridos");
        } else if (request.isbn().getIsbn().isBlank() || request.cpfUsuario().getCpf().isBlank()) {
                throw new SintaxeException("Impossível inserir campos vazios, preencha o cpf e o isbn corretamente");
        }


        Usuario usuarioReal = daoUsu.findByCpf(request.cpfUsuario().getCpf())
                .orElseThrow(() -> new NotFoundException("Este usuário não foi encontrado"));


        Livros livroReal = daoLivro.findByIsbn(request.isbn().getIsbn())
                .orElseThrow(() -> new NotFoundException("Este Livro não foi encontrado"));


        Optional<Emprestimos> validarUsuario = dao.findBycpfUsuario(usuarioReal);
        if (validarUsuario.isPresent()) {
            throw new ConflictException("Esse Usuário já possui um empréstimo!");
        }

        Optional<Emprestimos> validarLivro = dao.findByIsbn(livroReal);
        if (validarLivro.isPresent()) {
            throw new ConflictException("Este livro está indisponível para empréstimo");
        }

        Emprestimos salvarEmprestimo = new Emprestimos();

        salvarEmprestimo.setCpfUsuario(usuarioReal);
        salvarEmprestimo.setIsbn(livroReal);


        salvarEmprestimo.setDataSaida(LocalDateTime.now());
        salvarEmprestimo.setDataDevolucao(LocalDateTime.now().plusWeeks(2));

        dao.save(salvarEmprestimo);

        return new EmprestimoResponse(
                salvarEmprestimo.getId(),
                salvarEmprestimo.getDataSaida(),
                salvarEmprestimo.getDataDevolucao()
        );
    }

    public EmprestimoResponse buscar(Long id){
         Emprestimos validarEmp =dao.findById(id)
                 .orElseThrow(() -> new NotFoundException("Este empréstimo não foi encontrado"));

         return new EmprestimoResponse(
                 validarEmp.getId(),
                 validarEmp.getDataSaida(),
                 validarEmp.getDataDevolucao()
         );
    }

    public EmprestimoResponse deletar(Long id){
        Emprestimos validarEmp = dao.findById(id).orElse(null);
        if (validarEmp == null){
            throw new NotFoundException("Este empréstimo não foi encontrado");
        }

        if (validarEmp.getCpfUsuario() == null){
            dao.deleteById(id);
        } else {
            throw new ConflictException("Este úsuario ainda não devolveu o livro, impossível deletar o empréstimo ");
        }

        return new EmprestimoResponse(
                validarEmp.getId(),
                validarEmp.getDataSaida(),
                validarEmp.getDataDevolucao()
        );
    }

    public EmprestimoResponse atualizar(Long id, EmprestimoRequest request){


        Emprestimos validarEmp = dao.findById(id)
                .orElseThrow(() -> new NotFoundException("Este empréstimo não foi encontrado"));


        if(request.isbn() != null) {

            if(request.isbn().getIsbn().isBlank()){
                throw new SintaxeException("Impossível inserir um isbn vazio");
            }

            Livros livroReal = daoLivro.findByIsbn(request.isbn().getIsbn())
                    .orElseThrow(() -> new NotFoundException("Este Livro não foi encontrado"));


            Optional<Emprestimos> livroDisponivel = dao.findByIsbn(livroReal);

            if(livroDisponivel.isPresent() && !livroDisponivel.get().getId().equals(id)){
                throw new ConflictException("Este livro não está disponível para empréstimo");
            }

            validarEmp.setIsbn(livroReal);
        }

        if(request.cpfUsuario() != null) {

            if(request.cpfUsuario().getCpf().isBlank()){
                throw new SintaxeException("Impossível inserir um cpf vazio");
            }

            Usuario usuarioReal = daoUsu.findByCpf(request.cpfUsuario().getCpf())
                    .orElseThrow(() -> new NotFoundException("Este Usuario não foi encontrado"));

            Optional<Emprestimos> usuarioDisponivel = dao.findBycpfUsuario(usuarioReal);

            if(usuarioDisponivel.isPresent() && !usuarioDisponivel.get().getId().equals(id)){
                throw new ConflictException("Este Usuário já possui um empréstimo");
            }

            validarEmp.setCpfUsuario(usuarioReal);
        }


        dao.save(validarEmp);

        return new EmprestimoResponse(
                validarEmp.getId(),
                validarEmp.getDataSaida(),
                validarEmp.getDataDevolucao()
        );

    }

    public EmprestimoResponse devolucao(Long id){
        Emprestimos validarEmp =
                dao.findById(id).orElseThrow(()  -> new NotFoundException ("Empréstimo com este ID não foi encontrado"));

        validarEmp.setCpfUsuario(null);
        dao.save(validarEmp);
        return new EmprestimoResponse(
                validarEmp.getId(),
                validarEmp.getDataSaida(),
                validarEmp.getDataDevolucao()
        );
    }





    public EmprestimoResponse aumentarDevolucao(Long id){
        Emprestimos validarEmp = dao.findById(id)
                .orElseThrow(() -> new NotFoundException("Este empréstimo não foi encontrado"));

        validarEmp.setDataDevolucao(validarEmp.getDataDevolucao().plusWeeks(1));

        dao.save(validarEmp);

        return new EmprestimoResponse(
                validarEmp.getId(),
                validarEmp.getDataSaida(),
                validarEmp.getDataDevolucao()
        );
    }



}
