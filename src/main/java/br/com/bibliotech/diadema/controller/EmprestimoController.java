package br.com.bibliotech.diadema.controller;

import br.com.bibliotech.diadema.dto.request.EmprestimoRequest;
import br.com.bibliotech.diadema.dto.response.EmprestimoResponse;
import br.com.bibliotech.diadema.model.Emprestimos;
import br.com.bibliotech.diadema.service.EmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmprestimoController {

    private EmprestimoService service;

    @Autowired
    public EmprestimoController(EmprestimoService service) {
        this.service = service;
    }

    @PostMapping("/Emprestimo")
    public ResponseEntity<EmprestimoResponse> criarEmprestimo(@RequestBody EmprestimoRequest request){
            EmprestimoResponse response = service.criar(request);
            return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/Emprestimo/{id}")
    public ResponseEntity<EmprestimoResponse> buscarEmprestimo(@PathVariable Long id){
        EmprestimoResponse response = service.buscar(id);
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/Emprestimo/{id}")
    public ResponseEntity<EmprestimoResponse> deletarEmprestimo(@PathVariable Long id){
        EmprestimoResponse response = service.deletar(id);
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/Emprestimo/{id}")
    public ResponseEntity<EmprestimoResponse> atualizarEmprestimo(@PathVariable Long id, @RequestBody EmprestimoRequest request){
        EmprestimoResponse response = service.atualizar(id, request);
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/Emprestimo/Devolucao/{id}")
    public ResponseEntity<EmprestimoResponse> devolucaoEmprestimo(@PathVariable Long id){
        EmprestimoResponse response = service.devolucao(id);
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/Emprestimo/EstenderDevolucao/{id}")
    public ResponseEntity<EmprestimoResponse> estenderDevolucao(@PathVariable Long id){
        EmprestimoResponse response = service.aumentarDevolucao(id);
        return ResponseEntity.status(200).body(response);
    }



}
