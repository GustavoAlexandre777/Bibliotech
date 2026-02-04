package br.com.bibliotech.diadema.service;

import br.com.bibliotech.diadema.Exceptions.ConflictException;
import br.com.bibliotech.diadema.Exceptions.NotFoundException;
import br.com.bibliotech.diadema.Exceptions.NullException;
import br.com.bibliotech.diadema.Exceptions.SintaxeException;
import br.com.bibliotech.diadema.dao.IEmprestimoDao;
import br.com.bibliotech.diadema.dao.IUsuarioDao;
import br.com.bibliotech.diadema.dto.request.UsuarioRequest;
import br.com.bibliotech.diadema.dto.response.UsuarioResponse;
import br.com.bibliotech.diadema.model.Emprestimos;
import br.com.bibliotech.diadema.model.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class UsuarioService {

    private IUsuarioDao dao;
    private IEmprestimoDao daoEmprestimo;

    public UsuarioService(IUsuarioDao dao, IEmprestimoDao daoEmp) {
        super();
        this.dao = dao;
        this.daoEmprestimo = daoEmp;
    }

    public UsuarioResponse criar(UsuarioRequest request) {
        if(request.cpf() == null || request.nome() == null || request.email() == null){
            throw new NullException("Erro! Impossível criar um Usuario sem passar todos os dados");
        } else if (request.cpf().isBlank() || request.email().isBlank() || request.nome().isBlank()){
            throw new SintaxeException("Erro! Impossível inserir um atributo vazio, mande tudo preenchido");
        }

        Usuario cpf = dao.findByCpf(request.cpf().toLowerCase(Locale.ROOT)).orElse(null);
        if (cpf != null){
            throw new ConflictException("Esse usuário já foi cadastrado");
        }

        Usuario usuarioParaSalvar = new Usuario();

        usuarioParaSalvar.setId(null);
        usuarioParaSalvar.setNome(request.nome().toLowerCase(Locale.ROOT));
        usuarioParaSalvar.setCpf(request.cpf().toLowerCase(Locale.ROOT));
        usuarioParaSalvar.setEmail(request.email().toLowerCase(Locale.ROOT));


        Usuario usuarioSalvo = dao.save(usuarioParaSalvar);

        return new UsuarioResponse(
            usuarioSalvo.getNome(),
            usuarioSalvo.getEmail(),
            usuarioSalvo.getId()

        );
    }

    public UsuarioResponse procurar(Long id){
        Usuario usuario = dao.findById(id)
                .orElseThrow(() -> new NotFoundException("Usúario não encontrado"));

        return new UsuarioResponse(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getId()
        );
    }

    @Modifying
    @Transactional
    public UsuarioResponse deletar(String cpf){
        Usuario validar = dao.findByCpf(cpf)
                .orElseThrow(() -> new NotFoundException("Esse Usuário não foi encontrado"));

        Emprestimos validarEmp = daoEmprestimo.findByCpfUsuarioCpf(cpf).orElse(null);

        if(validarEmp != null){
            throw new ConflictException("Impossível remover este usuário, porque ele possui empréstimos pendentes");
        }

        UsuarioResponse backup = new UsuarioResponse(
                validar.getNome(),
                validar.getEmail(),
                validar.getId()

        );


        dao.deleteByCpf(cpf);

        return backup;
    }


    public UsuarioResponse atualizar(Long id, UsuarioRequest request){

        Usuario usuarioExistente = dao.findById(id)
                .orElseThrow(() -> new NotFoundException("Este Usuário não foi encontrado"));


        if (request.nome() == null && request.email() == null && request.cpf() == null){
            throw new NullException("Erro! Impossível atualizar, você não inseriu nenhum valor");
        }


        if(request.nome() != null){
            if(request.nome().isBlank()){
                throw new SintaxeException("Erro! campo nome está vazio");
            }
            usuarioExistente.setNome(request.nome().toLowerCase(Locale.ROOT));
        }


        if(request.email() != null){
            if(request.email().isBlank()){
                throw new SintaxeException("Erro! campo de email esta vazio");
            }


            dao.findByEmail(request.email().toLowerCase(Locale.ROOT))
                    .ifPresent(u -> {
                        if(!u.getId().equals(id)) {
                            throw new ConflictException("Erro! este email já está em uso por outro usuário");
                        }
                    });

            usuarioExistente.setEmail(request.email().toLowerCase(Locale.ROOT));
        }


        if (request.cpf() != null){
            if(request.cpf().isBlank()){
                throw new SintaxeException("Erro! campo de cpf está vazio");
            }
            usuarioExistente.setCpf(request.cpf().toLowerCase(Locale.ROOT));
        }


        Usuario usuarioAtualizado = dao.save(usuarioExistente);

        return new UsuarioResponse(
                usuarioAtualizado.getNome(),
                usuarioAtualizado.getEmail(),
                usuarioAtualizado.getId()
        );
    }



}