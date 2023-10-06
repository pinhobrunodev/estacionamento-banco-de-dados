package br.com.ucsal.estacionamento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor

@NoArgsConstructor
@Data
public class VagasStatusDTO {
    private Long id;
    private String placa;
    private String horaEntrada;
}
