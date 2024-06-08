package br.com.ucsal.estacionamento.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NaoTemMaisVagasException.class)
    public ResponseEntity<ErroResposta> handleProductException(NaoTemMaisVagasException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroResposta(e.getMessage()));
    }
}
