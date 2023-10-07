package br.com.ucsal.estacionamento.dto;

import br.com.ucsal.estacionamento.entity.ClientesMensalistas;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrazerClientesMensalistasCriadosDTO {
    private Long id;
    private String cpf;

    public TrazerClientesMensalistasCriadosDTO(ClientesMensalistas clientesMensalistas) {
        id = clientesMensalistas.getId();
        cpf = clientesMensalistas.getCpf();
    }
}
