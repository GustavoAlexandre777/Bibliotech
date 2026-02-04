package br.com.bibliotech.diadema.service;


import br.com.bibliotech.diadema.Exceptions.ConflictException;
import br.com.bibliotech.diadema.Exceptions.NotFoundException;
import br.com.bibliotech.diadema.Exceptions.NullException;
import br.com.bibliotech.diadema.Exceptions.SintaxeException;
import br.com.bibliotech.diadema.dao.ICategoriaDao;
import br.com.bibliotech.diadema.dto.request.CategoriaRequest;
import br.com.bibliotech.diadema.dto.response.CategoriaResponse;
import br.com.bibliotech.diadema.model.Categorias;
import br.com.bibliotech.diadema.model.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletionException;

@Service
public class CategoriaService {

    private ICategoriaDao dao;

    public CategoriaService(ICategoriaDao dao) {
        super();
        this.dao = dao;
    }

    public CategoriaResponse criar(CategoriaRequest request) {


        if (request.nome() == null) {
            throw new NullException("Impossível inserir um valor vazio");
        }

        if (request.nome().isBlank()) {
            throw new SintaxeException("Erro! Impossível salvar uma categoria sem nome");
        }


        dao.findByNome(request.nome().toLowerCase(Locale.ROOT))
                .ifPresent(c -> {
                    throw new ConflictException("Já existe uma categoria com este nome");
                });


        Categorias novaCategoria = new Categorias();
        novaCategoria.setNome(request.nome().toLowerCase(Locale.ROOT));

        Categorias categoriaSalva = dao.save(novaCategoria);

        return new CategoriaResponse(
                categoriaSalva.getId(),
                categoriaSalva.getNome()
        );
    }


    public CategoriaResponse procurar(Long id){
            Categorias categoria = dao.findById(id)
                    .orElseThrow(() -> new NotFoundException("Categoria não existe"));

            return new CategoriaResponse(
                    categoria.getId(),
                    categoria.getNome()
            );

    }

    public CategoriaResponse apagar(Long id){

        Categorias validar = dao.findById(id)
                .orElseThrow(() -> new NotFoundException("Essa categoria não foi encontrada"));



        CategoriaResponse backup = new CategoriaResponse(
                validar.getId(),
                validar.getNome()
        );

        dao.deleteById(id);

        return backup;

    }

    public CategoriaResponse atualizar(Long id, CategoriaRequest request) {

        Categorias categ = dao.findById(id)
                .orElseThrow(() -> new NotFoundException("Essa categoria não foi encontrada"));


        Optional<Categorias> categoriaComMesmoNome = dao.findByNome(request.nome());

        if (categoriaComMesmoNome.isPresent()) {
            throw new ConflictException("Já existe uma categoria com este nome!");
        } else if (request.nome().isBlank()){
            throw new NullException("Erro! impossível inserir um dado nulo");
        }

        categ.setNome(request.nome().toLowerCase(Locale.ROOT));

        CategoriaResponse response = new CategoriaResponse(
                categ.getId(),
                categ.getNome()
        );

        dao.save(categ);

        return  response;
    }




}
