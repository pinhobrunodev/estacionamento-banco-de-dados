package br.com.ucsal.estacionamento.controllers;

import br.com.ucsal.estacionamento.services.VagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VagaController {

    @Autowired
    private VagaService vagaService;


    @GetMapping(value = "/situacao-vagas")
    public ResponseEntity<?> trazerVagas(){
        return ResponseEntity.ok(vagaService.situacaoVagas());
    }
}
