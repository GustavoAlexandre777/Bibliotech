package br.com.bibliotech.diadema.controller;


import br.com.bibliotech.diadema.dto.request.AutoresRequest;
import br.com.bibliotech.diadema.dto.response.AutoresResponse;
import br.com.bibliotech.diadema.model.Autores;
import br.com.bibliotech.diadema.service.AutoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AutoresController {

    private AutoresService service;

    @Autowired
    public AutoresController(AutoresService service) {
        this.service = service;
    }

    @PostMapping("/Autores")
    public ResponseEntity<AutoresResponse> criarAutor(@RequestBody AutoresRequest request){
        AutoresResponse response = service.criar(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/Autores/{id}")
    public ResponseEntity<AutoresResponse> buscarAutor(@PathVariable Long id){
        AutoresResponse response = service.procurar(id);
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/Autores/{id}")
    public ResponseEntity<AutoresResponse> deletarAutor(@PathVariable Long id){
       AutoresResponse response = service.deletar(id);
       return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/Autores/{id}")
    public ResponseEntity<AutoresResponse> atualizarAutor(@PathVariable Long id, @RequestBody AutoresRequest request){
        AutoresResponse response = service.atualizar(id, request);
        return ResponseEntity.status(200).body(response);
    }


}
