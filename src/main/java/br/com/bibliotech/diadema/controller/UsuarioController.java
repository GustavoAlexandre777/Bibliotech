package br.com.bibliotech.diadema.controller;

import br.com.bibliotech.diadema.dto.request.UsuarioRequest;
import br.com.bibliotech.diadema.dto.response.UsuarioResponse;
import br.com.bibliotech.diadema.model.Usuario;
import br.com.bibliotech.diadema.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsuarioController {

    private UsuarioService service;

    @Autowired
    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/Usuario")
    public ResponseEntity<UsuarioResponse> criarUsuario(@RequestBody UsuarioRequest request){
        UsuarioResponse response = service.criar(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/Usuario/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuario(@PathVariable Long id){
        UsuarioResponse response = service.procurar(id);
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/Usuario/{id}")
    public ResponseEntity<UsuarioResponse> deletarUsuario(@RequestParam String cpf){
        UsuarioResponse response = service.deletar(cpf);
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/Usuario/{id}")
    public ResponseEntity<UsuarioResponse> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequest request){
        UsuarioResponse response = service.atualizar(id, request);
        return ResponseEntity.status(200).body(response);
    }
}
