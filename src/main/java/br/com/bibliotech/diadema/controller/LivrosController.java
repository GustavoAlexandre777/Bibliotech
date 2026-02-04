package br.com.bibliotech.diadema.controller;

import br.com.bibliotech.diadema.dto.request.AtualizarAutoresRequest;
import br.com.bibliotech.diadema.dto.request.LivroRequest;
import br.com.bibliotech.diadema.dto.request.VincularAutorRequest;
import br.com.bibliotech.diadema.dto.response.LivroResponse;
import br.com.bibliotech.diadema.dto.response.VincularAutorResponse;
import br.com.bibliotech.diadema.model.Livros;
import br.com.bibliotech.diadema.service.LivrosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LivrosController {

    private LivrosService service;

    @Autowired
    public LivrosController(LivrosService service) {
        this.service = service;
    }

    @PostMapping("/Livro")
    public ResponseEntity<LivroResponse> criarLivro(@RequestBody LivroRequest request){
        LivroResponse response = service.criar(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/Livro/{id}")
    public ResponseEntity<LivroResponse> buscarLivro(@PathVariable Long id){
        LivroResponse response = service.buscar(id);
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/Livro/{id}")
    public ResponseEntity<LivroResponse> deletarLivro(@PathVariable Long id, @RequestParam String isbn){
        LivroResponse response = service.deletar(id, isbn);
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/Livro/{id}")
    public ResponseEntity<LivroResponse> atualizarLivro(@PathVariable Long id, @RequestBody LivroRequest request){
        LivroResponse response = service.atualizar(id, request);
        return ResponseEntity.status(200).body(response);
    }


    @PostMapping("/Livro/InserirAutor")
    public ResponseEntity<VincularAutorResponse> vinculaAutor(@RequestBody VincularAutorRequest request){
            VincularAutorResponse response = service.vincularAutor(request);
            return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/Livro/AtualizarAutor")
    public ResponseEntity<VincularAutorResponse> atualizarAutor(@RequestBody AtualizarAutoresRequest request){
        VincularAutorResponse response = service.atualizarAutores(request);
        return ResponseEntity.status(200).body(response);
    }





}
