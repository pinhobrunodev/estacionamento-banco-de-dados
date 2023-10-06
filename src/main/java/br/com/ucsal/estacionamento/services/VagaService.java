package br.com.ucsal.estacionamento.services;

import br.com.ucsal.estacionamento.dto.TrazerVagasStatusDTO;
import br.com.ucsal.estacionamento.dto.VagasStatusDTO;
import br.com.ucsal.estacionamento.repositories.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class VagaService {

    @Autowired
    private VagaRepository vagaRepository;


    @Transactional(readOnly = true)
    public List<TrazerVagasStatusDTO> trazerStatusDasVagas() {
        List<TrazerVagasStatusDTO> trazerVagasStatusDTOList = new ArrayList<>();
        List<VagasStatusDTO> statusVagasHorista = new ArrayList<>();
        List<VagasStatusDTO> statusVagasMensalista = new ArrayList<>();
        Long quantidadeVagasRestantesHorista = vagaRepository.quantidadeVagasHoristasLivre();
        Long quantidadeVagasRestantesMensalista = vagaRepository.quantidadeVagasMensalistasLivre();
        List<Object[]> resultadoBusca = vagaRepository.capturaStatusDasVagas();
        for (Object[] row : resultadoBusca) {
            VagasStatusDTO vagasStatusDTO = new VagasStatusDTO();
            String tipoStatus = (String) row[0];
            vagasStatusDTO.setId((Long) row[1]);
            vagasStatusDTO.setPlaca((String) row[2]);
            vagasStatusDTO.setHoraEntrada((String) row[3]);
            switch (tipoStatus) {
                case "ocupadoHorista" -> statusVagasHorista.add(vagasStatusDTO);
                case "ocupadoMensalista" -> statusVagasMensalista.add(vagasStatusDTO);
            }
        }
        if (!statusVagasHorista.isEmpty()) {
            trazerVagasStatusDTOList.add(new TrazerVagasStatusDTO("VAGAS HORISTAS", quantidadeVagasRestantesHorista, statusVagasHorista));
        }
        if (!statusVagasMensalista.isEmpty()) {
            trazerVagasStatusDTOList.add(new TrazerVagasStatusDTO("VAGAS MENSALISTAS", quantidadeVagasRestantesMensalista, statusVagasMensalista));
        }
        return trazerVagasStatusDTOList;
    }
}
