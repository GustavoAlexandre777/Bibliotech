package br.com.bibliotech.diadema.service;

import br.com.bibliotech.diadema.Exceptions.ConflictException;
import br.com.bibliotech.diadema.Exceptions.NotFoundException;
import br.com.bibliotech.diadema.Exceptions.NullException;
import br.com.bibliotech.diadema.Exceptions.SintaxeException;
import br.com.bibliotech.diadema.dao.IAutoresDao;
import br.com.bibliotech.diadema.dao.ICategoriaDao;
import br.com.bibliotech.diadema.dao.IEmprestimoDao;
import br.com.bibliotech.diadema.dao.ILivrosDao;
import br.com.bibliotech.diadema.dto.request.AtualizarAutoresRequest;
import br.com.bibliotech.diadema.dto.request.LivroRequest;
import br.com.bibliotech.diadema.dto.request.VincularAutorRequest;
import br.com.bibliotech.diadema.dto.response.LivroResponse;
import br.com.bibliotech.diadema.dto.response.VincularAutorResponse;
import br.com.bibliotech.diadema.model.Autores;
import br.com.bibliotech.diadema.model.Categorias;
import br.com.bibliotech.diadema.model.Emprestimos;
import br.com.bibliotech.diadema.model.Livros;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class LivrosService {

    private ILivrosDao dao;
    private ICategoriaDao daoCategoria;
    private IAutoresDao daoAutores;
    private IEmprestimoDao daoEmprestimo;

    public LivrosService(ILivrosDao dao, ICategoriaDao daoCateg, IAutoresDao daoAut, IEmprestimoDao daoEmp) {
        super();
        this.dao = dao;
        this.daoCategoria = daoCateg;
        this.daoAutores = daoAut;
        this.daoEmprestimo = daoEmp;

    }

    public LivroResponse criar(LivroRequest request){

        if (request.isbn() == null || request.idCategoria() == null || request.isbn() == null || request.dataPublicacao() == null){ //colocar autores
            throw  new NullException("Erro! todos os dados devem ser preenchidos");
        } else if (request.titulo().isBlank() ||  request.isbn().isBlank()){ //colocar autores
            throw  new NullException("Erro! Impossível inserir campos com espaços vazios");
        }


        Categorias validarCat = daoCategoria.findById(request.idCategoria().getId())
                .orElseThrow(() -> new NotFoundException("Está categoria não foi encontrada"));


        Livros codigo = dao.findByIsbn(request.isbn().toLowerCase(Locale.ROOT)).orElse(null);
        if (codigo != null){
            throw new ConflictException("Esse Livro já foi registrado");
        }


        Livros livroParaSalvar = new Livros();

        livroParaSalvar.setId(null);
        livroParaSalvar.setTitulo(request.titulo().toLowerCase(Locale.ROOT));
        livroParaSalvar.setIsbn(request.isbn().toLowerCase(Locale.ROOT));
        livroParaSalvar.setDataPublicacao(request.dataPublicacao());
        livroParaSalvar.setIdCategoria(request.idCategoria());

        Livros livroSalvo = dao.save(livroParaSalvar);

        return new LivroResponse(
                livroSalvo.getId(),
                livroSalvo.getTitulo()
        );
    }

    public LivroResponse buscar(Long id){
        Livros livro = dao.findById(id)
                .orElseThrow(() -> new NotFoundException("Este livro não foi encontrado"));

        return new LivroResponse(
                livro.getId(),
                livro.getTitulo()
        );

    }


    @Transactional
    @Modifying
    public LivroResponse deletar(Long id, String isbn){
        Livros validar = dao.findById(id)
                .orElseThrow(() -> new NotFoundException("Este livro não foi encontrado"));

        Emprestimos validarEmp = daoEmprestimo.findByIsbnIsbn(isbn).orElse(null);

        if(validarEmp != null){
            throw new ConflictException("Este livro não pode ser apagado porque está vinculado a um empréstimo");
        }

        LivroResponse backup = new LivroResponse(
                validar.getId(),
                validar.getTitulo()
        );

        dao.deleteById(id);


        return backup;
    }

    @Transactional
    public LivroResponse atualizar(Long id, LivroRequest request){

        Livros validarLivro = dao.findById(id)
                .orElseThrow(() -> new NotFoundException("Este livro não foi encontrado"));

        if(request.titulo() != null){
            if(request.titulo().isBlank()){
                throw new SintaxeException("Erro! impossível atualizar o titulo porque o campo está vazio");
            }
            validarLivro.setTitulo(request.titulo().toLowerCase(Locale.ROOT));
        }

        if(request.isbn() != null){
            if(request.isbn().isBlank()) {
                throw new NullException("Erro! impossível atualizar o isbn porque o campo está vazio");
            } else {
                if(dao.findByIsbn(request.isbn()).orElse(null) != null){
                    throw new ConflictException("Erro! este isbn já está cadastrado");
                }
                validarLivro.setIsbn(request.isbn().toLowerCase(Locale.ROOT));
            }
        }

        if(request.idCategoria() != null){
            if(daoCategoria.findById(request.idCategoria().getId()).orElse(null) == null){
                throw new NullException("Erro! você está tentando atribuir o livro a uma categoria que não existe em nosso sistema");
            }
            validarLivro.setIdCategoria(request.idCategoria());
        }



        if(request.dataPublicacao() != null){
            validarLivro.setDataPublicacao(request.dataPublicacao());
        }

        dao.save(validarLivro);

        return new LivroResponse(
                validarLivro.getId(),
                validarLivro.getTitulo()
        );

    }




    public String vincularAutores(Long idAutor, Long idLivro){

        if (idAutor == null || idLivro == null){
            throw new NullException("Ids não podem ser nulos");
        }

        Autores validarAutor = daoAutores.findById(idAutor)
                .orElseThrow(() -> new NotFoundException("Este autor não foi encontrado"));

        Livros validarLivro =  dao.findById(idLivro)
                .orElseThrow(() -> new NotFoundException("Este Livro não foi encontrado"));


        if (validarLivro.getAutores().contains(validarAutor)){
            throw new ConflictException("Este autor já está vinculado a este livro");
        }

        validarLivro.getAutores().add(validarAutor);
        validarAutor.getLivros().add(validarLivro);

        dao.save(validarLivro);

        return "Autor(es) vinculado(s) com sucesso";
    }


    public String  atualizarAutor(Long idAutor, Long idLivro){

        if(idAutor == null || idLivro == null){
            throw new NullException("Erro! nenhum Id pode ser nulo para atualização");
        }

        Autores validaraut = daoAutores.findById(idAutor)
                .orElseThrow(() -> new NotFoundException("Este autor não foi encontrado"));

        Livros validarAtt = dao.findById(idLivro)
                .orElseThrow(() -> new NotFoundException("Este livro não foi encontrado"));



        validarAtt.getAutores().add(validaraut);


        dao.save(validarAtt);

        return "Atualizado com sucesso!";

    }


    @Transactional
    public VincularAutorResponse vincularAutor(VincularAutorRequest request){
        Livros validarLivro = dao.findById(request.idLivro())
                .orElseThrow(() -> new NotFoundException("Este livro não foi encontrado"));

        Autores validarAutor = daoAutores.findById(request.idAutor())
                .orElseThrow(() -> new NotFoundException("Este Autor não foi encontrado"));

        if(!validarLivro.getAutores().contains(validarAutor)){
            validarLivro.getAutores().add(validarAutor);
        } else {
            throw new ConflictException("Este autor já está vinculado a este livro");
        }

        dao.save(validarLivro);

        List<String> nomes = validarLivro.getAutores().stream()
                                         .map(Autores::getNome)
                                         .toList();

        return new VincularAutorResponse(
                validarLivro.getTitulo(),
                nomes
        );
    }


    @Transactional
    public VincularAutorResponse atualizarAutores(AtualizarAutoresRequest request){
        Livros validarlivro = dao.findById(request.idLivro())
                .orElseThrow(() -> new NotFoundException("Este livro não foi encontrado"));

        List<Autores> novosAutores = (List<Autores>) daoAutores.findAllById(request.idsAutores());

        if (novosAutores.isEmpty()){
            throw new NotFoundException("Nenhum dos autores informados foi encontrado");
        }

        validarlivro.getAutores().clear();

        validarlivro.getAutores().addAll(novosAutores);

        List<String> nomes = validarlivro.getAutores().stream()
                                         .map(Autores::getNome)
                                         .toList()  ;


        return new VincularAutorResponse(validarlivro.getTitulo(), nomes);
    }



}
