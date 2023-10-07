package br.com.ucsal.estacionamento.controllers;

import br.com.ucsal.estacionamento.dto.RegistrarMensalistaDTO;
import br.com.ucsal.estacionamento.dto.TrazerClientesMensalistasEstacionados;
import br.com.ucsal.estacionamento.services.ClienteMensalistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClienteMensalistaController {

    @Autowired
    private ClienteMensalistaService service;


    @PostMapping(value = "/criar-mensalista")
    private ResponseEntity<Void> criarMensalista(@RequestBody RegistrarMensalistaDTO registrarMensalistaDTO) {
        service.criarMensalista(registrarMensalistaDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(value = "/registrar-entrada-mensalista")
    private ResponseEntity<Void> registrarEntrada(@RequestBody String cpf) {
        service.registrarEntrada(cpf);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping(value = "/registrar-saida-mensalista/{cpf}")
    private ResponseEntity<Void> registrarSaida(@PathVariable String cpf) {
        service.registrarSaida(cpf);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/trazer-mensalistas-estacionados")
    public ResponseEntity<List<TrazerClientesMensalistasEstacionados>> trazerMensalistasEstacionados(){
        return ResponseEntity.ok(service.trazerMensalistasEstacionados());
    }
}
