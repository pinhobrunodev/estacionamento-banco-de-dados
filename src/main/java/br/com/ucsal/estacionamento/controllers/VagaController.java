package br.com.ucsal.estacionamento.controllers;

import br.com.ucsal.estacionamento.dto.TrazerVagasStatusDTO;
import br.com.ucsal.estacionamento.services.VagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VagaController {

    @Autowired
    private VagaService vagaService;

    @GetMapping(value = "/status-vagas")
    public ResponseEntity<List<TrazerVagasStatusDTO>> capturaStatusDasVagas() {
        return ResponseEntity.ok(vagaService.trazerStatusDasVagas());
    }
}
