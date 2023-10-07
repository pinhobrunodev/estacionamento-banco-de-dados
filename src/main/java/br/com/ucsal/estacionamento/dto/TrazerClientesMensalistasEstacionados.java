package br.com.ucsal.estacionamento.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrazerClientesMensalistasEstacionados {
    private Long id;
    private String placa;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:MM:ss")
    private Date dataHoraEntrada;
    private String cpf;


}
