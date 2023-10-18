package br.com.ucsal.estacionamento.services;

import br.com.ucsal.estacionamento.dto.TrazerClientesHoristasEstacionados;
import br.com.ucsal.estacionamento.entity.PlacasHoristas;
import br.com.ucsal.estacionamento.entity.PlacasMensalistas;
import br.com.ucsal.estacionamento.entity.TabelaPreco;
import br.com.ucsal.estacionamento.entity.Vaga;
import br.com.ucsal.estacionamento.repositories.ClienteHoristaRepository;
import br.com.ucsal.estacionamento.repositories.PlacasMensalistasRepository;
import br.com.ucsal.estacionamento.repositories.TabelaPrecoRepository;
import br.com.ucsal.estacionamento.repositories.VagaRepository;
import br.com.ucsal.estacionamento.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ClienteHoristaService {

    @Autowired
    private ClienteHoristaRepository clienteHoristaRepository;
    @Autowired
    private PlacasMensalistasRepository placasMensalistasRepository;
    @Autowired
    private TabelaPrecoRepository tabelaPrecoRepository;
    @Autowired
    private VagaRepository vagaRepository;

    @Transactional
    public void registrarEntrada(String placa) {
        Long vagasLivresHorista = vagaRepository.quantidadeVagasHoristasLivre();
        if (vagasLivresHorista == 0) {
            throw new RuntimeException("NAO TEM MAIS VAGA HORISTA");
        }
        PlacasHoristas placasHoristas = new PlacasHoristas();
        placasHoristas.setPlaca(placa);
        placasHoristas.setEntrada(LocalDateTime.now());
        placasHoristas.setSaida(null);
        Vaga vagaSelecionada = vagaRepository.selecionaUmaVagaHorista();
        vagaSelecionada.setOcupada(Boolean.TRUE);
        vagaSelecionada.setPlaca(placa);
        clienteHoristaRepository.save(placasHoristas);
        vagaRepository.save(vagaSelecionada);
    }

    @Transactional
    public String registrarSaida(String placa) {
        boolean placaMensalista = placasMensalistasRepository.existsByPlaca(placa);
        if (placaMensalista) {
            Vaga vagaQueFicouLivre = vagaRepository.capturaVagaPelaPlaca(placa);
            vagaQueFicouLivre.setPlaca(null);
            vagaQueFicouLivre.setOcupada(Boolean.FALSE);
            vagaRepository.save(vagaQueFicouLivre);
            return "Não é necessário pagar a taxa, pois é um cliente mensalista que saiu de uma vaga horista";
        }
        PlacasHoristas placaHorista = clienteHoristaRepository.findById(placa).orElseThrow(() -> new RuntimeException("n achou"));
        placaHorista.setSaida(LocalDateTime.now());
        TabelaPreco tabelaPrecoHorista = tabelaPrecoRepository.findById(2L).orElseThrow(() -> new RuntimeException("n achou"));
        clienteHoristaRepository.save(placaHorista);
        Vaga vagaQueFicouLivre = vagaRepository.capturaVagaPelaPlaca(placa);
        vagaQueFicouLivre.setPlaca(null);
        vagaQueFicouLivre.setOcupada(Boolean.FALSE);
        vagaRepository.save(vagaQueFicouLivre);
        Double valorAPagar = calculaPrecoFinalHorista(placaHorista.getEntrada(), placaHorista.getSaida(), tabelaPrecoHorista);
        return Utils.formataMensagemSaida(placa, placaHorista.getEntrada(), placaHorista.getSaida(), valorAPagar);
    }

    private Double calculaPrecoFinalHorista(LocalDateTime dataHrEntrada, LocalDateTime dataHrSaida, TabelaPreco tabelaPrecoHorista) {
        Duration duracao = Duration.between(dataHrEntrada, dataHrSaida);
        if (duracao.toHours() < 1) {
            return tabelaPrecoHorista.getPreco();
        }
        return duracao.toHours() * tabelaPrecoHorista.getPreco();
    }

    @Transactional(readOnly = true)
    public List<TrazerClientesHoristasEstacionados> trazerHoristasEstacionados() {
        List<TrazerClientesHoristasEstacionados> horistasEstacionados = new ArrayList<>();
        List<Object[]> resultado = vagaRepository.capturaHoristasEstacionados();
        List<Object[]> resultadoMensalistasEmVagasDeHorista = vagaRepository.capturaMensalistasEmVagasDeHorista();
        for (Object[] linha : resultado) {
            String placa = (String) linha[1];
            TrazerClientesHoristasEstacionados trazerClientesHoristasEstacionados = new TrazerClientesHoristasEstacionados();
            trazerClientesHoristasEstacionados.setId((Long) linha[0]);
            trazerClientesHoristasEstacionados.setPlaca(placa);
            trazerClientesHoristasEstacionados.setDataHoraEntrada((Date) linha[2]);
            trazerClientesHoristasEstacionados.setIsHorista(false);
            horistasEstacionados.add(trazerClientesHoristasEstacionados);
        }
        for (Object[] linha2 : resultadoMensalistasEmVagasDeHorista) {
            String placa = (String) linha2[1];
            TrazerClientesHoristasEstacionados trazerClientesHoristasEstacionados = new TrazerClientesHoristasEstacionados();
            trazerClientesHoristasEstacionados.setId((Long) linha2[0]);
            trazerClientesHoristasEstacionados.setPlaca(placa);
            trazerClientesHoristasEstacionados.setDataHoraEntrada((Date) linha2[2]);
            trazerClientesHoristasEstacionados.setIsHorista(true);
            horistasEstacionados.add(trazerClientesHoristasEstacionados);
        }
        return horistasEstacionados;
    }
}
