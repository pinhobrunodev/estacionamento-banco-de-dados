package br.com.ucsal.estacionamento.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaoTemMaisVagasException extends RuntimeException {

    private String message;

    public NaoTemMaisVagasException(String message){
        this.message = message;
    }
}
