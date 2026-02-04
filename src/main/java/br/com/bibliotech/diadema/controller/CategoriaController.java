package br.com.bibliotech.diadema.controller;


import br.com.bibliotech.diadema.dto.request.CategoriaRequest;
import br.com.bibliotech.diadema.dto.response.CategoriaResponse;
import br.com.bibliotech.diadema.model.Categorias;
import br.com.bibliotech.diadema.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoriaController {

    private CategoriaService service;

    @Autowired
    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @PostMapping("/Categoria")
    public ResponseEntity<CategoriaResponse> criarCategoria(@RequestBody CategoriaRequest request){
        CategoriaResponse response = service.criar(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/Categoria/{id}")
    public ResponseEntity<CategoriaResponse> buscarCategoria(@PathVariable Long id){
        CategoriaResponse response = service.procurar(id);
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/Categoria/{id}")
    public ResponseEntity<CategoriaResponse> deletarCategoria(@PathVariable Long id){
        CategoriaResponse response = service.apagar(id);
        return ResponseEntity.status(200).body(response);
    }


    @PutMapping("/Categoria/{id}")
    public ResponseEntity<CategoriaResponse> atualizarCategoria(@PathVariable Long id, @RequestBody CategoriaRequest request){
        CategoriaResponse response = service.atualizar(id, request);
        return ResponseEntity.status(200).body(response);
    }





}
