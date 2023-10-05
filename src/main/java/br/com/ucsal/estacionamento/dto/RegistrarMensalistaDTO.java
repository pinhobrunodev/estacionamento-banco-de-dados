package br.com.ucsal.estacionamento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegistrarMensalistaDTO {
    private String nome;
    private String cpf;
    private String tel;
    private List<PlacaMensalistaDTO> placas = new ArrayList<>();
}
