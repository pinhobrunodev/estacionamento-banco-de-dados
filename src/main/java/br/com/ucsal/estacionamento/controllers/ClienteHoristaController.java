package br.com.ucsal.estacionamento.controllers;

import br.com.ucsal.estacionamento.services.ClienteHoristaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClienteHoristaController {

    @Autowired
    private ClienteHoristaService service;

    @PostMapping(value = "/registrar-entrada-horista")
    private ResponseEntity<Void> registrarEntrada(@RequestBody String placa) {
        service.registrarEntrada(placa);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping(value = "/registrar-saida-horista/{placa}")
    private ResponseEntity<String> registrarSaida(@PathVariable String placa) {
        return ResponseEntity.ok(service.registrarSaida(placa));
    }
}
