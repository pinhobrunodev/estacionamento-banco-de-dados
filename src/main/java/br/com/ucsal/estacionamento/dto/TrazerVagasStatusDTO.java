package br.com.ucsal.estacionamento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrazerVagasStatusDTO {

    private String categoria;
    private Long vagasRestantes;
    private List<VagasStatusDTO> status;


}
