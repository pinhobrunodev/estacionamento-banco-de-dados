package br.com.ucsal.estacionamento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SituacaoVagasDTO {
    private Long vagasHoristasLivre;
    private Long vagasHoristasPreenchida;
    private Long vagasMensalistasLivre;
    private Long vagasMensalistasPreenchida;
}
