package br.com.ucsal.estacionamento.services;

import br.com.ucsal.estacionamento.dto.SituacaoVagasDTO;
import br.com.ucsal.estacionamento.repositories.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VagaService {

    @Autowired
    private VagaRepository vagaRepository;


    @Transactional(readOnly = true)
    public SituacaoVagasDTO situacaoVagas() {
        Long quantidadeVagasRestantesHorista = vagaRepository.quantidadeVagasHoristasLivre();
        Long quantidadeVagasRestantesMensalista = vagaRepository.quantidadeVagasMensalistasLivre();
        Long quantidadeVagasHoristaOcupado = vagaRepository.quantidadeVagasHoristasOcupado();
        Long quantidadeVagasMensalistaOcupado = vagaRepository.quantidadeVagasMensalistasOcupado();
        return new SituacaoVagasDTO(quantidadeVagasRestantesHorista,quantidadeVagasHoristaOcupado,quantidadeVagasRestantesMensalista,quantidadeVagasMensalistaOcupado);
    }
}
