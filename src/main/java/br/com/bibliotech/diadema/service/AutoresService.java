package br.com.bibliotech.diadema.service;

import br.com.bibliotech.diadema.Exceptions.ConflictException;
import br.com.bibliotech.diadema.Exceptions.NotFoundException;
import br.com.bibliotech.diadema.Exceptions.NullException;
import br.com.bibliotech.diadema.Exceptions.SintaxeException;
import br.com.bibliotech.diadema.dao.IAutoresDao;
import br.com.bibliotech.diadema.dto.request.AutoresRequest;
import br.com.bibliotech.diadema.dto.response.AutoresResponse;
import br.com.bibliotech.diadema.model.Autores;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AutoresService {

    private IAutoresDao dao;

    public AutoresService(IAutoresDao dao) {
        super();
        this.dao = dao;
    }

    public AutoresResponse criar(AutoresRequest request) {

        if(request.nome() == null || request.nacionalidade() == null){
            throw new NullException("Erro! impossível inserir valores nulos");
        } else if (request.nome().isBlank() || request.nacionalidade().isBlank()){
            throw new NullException("Erro! você está tentando inserir espaços em brnacos, coloque dados verdadeiros");
        }

        Autores autorParaSalvar = new Autores();

        autorParaSalvar.setId(null);
        autorParaSalvar.setNome(request.nome().toLowerCase(Locale.ROOT));
        autorParaSalvar.setNacionalidade(request.nacionalidade().toLowerCase(Locale.ROOT));

        dao.save(autorParaSalvar);

        return new AutoresResponse(
                autorParaSalvar.getId(),
                autorParaSalvar.getNome(),
                autorParaSalvar.getNacionalidade()
        );
    }


    public AutoresResponse procurar(Long id){
        Autores validarAut = dao.findById(id)
                                    .orElseThrow(() -> new NotFoundException("Este Autor não existe"));

        return new AutoresResponse(
                validarAut.getId(),
                validarAut.getNome(),
                validarAut.getNacionalidade()
        );
    }

    public AutoresResponse deletar(Long id){
        Autores validar = dao.findById(id)
                .orElseThrow(() -> new NotFoundException("Este autor não foi encontrado"));

        dao.deleteById(id);

        return new AutoresResponse(
                validar.getId(),
                validar.getNome(),
                validar.getNacionalidade()
        );
    }

    public AutoresResponse atualizar(Long id, AutoresRequest request){
        Autores aut = dao.findById(id)
                .orElseThrow(() -> new NotFoundException("Este Autor não foi encontrado"));

        if(request.nome() != null){
            if(request.nome().isBlank()){
                throw new SintaxeException("Impossível inserir um nome vazio");
            }
            aut.setNome(request.nome().toLowerCase(Locale.ROOT));
        }

        if (request.nacionalidade() != null){
            if (request.nacionalidade().isBlank()){
                throw new SintaxeException("Impossível inserir uma nacionalidade vazia");
            }

            aut.setNacionalidade(request.nacionalidade().toLowerCase(Locale.ROOT));
        }

        if(request.nome() == null && request.nacionalidade() == null){
            throw new NullException("Erro! nenhum valor foi inserido para atualizar o autor");
        }

        dao.save(aut);

        return new AutoresResponse(
                aut.getId(),
                aut.getNome(),
                aut.getNacionalidade()
        );
    }


}