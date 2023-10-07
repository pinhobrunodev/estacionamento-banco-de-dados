package br.com.ucsal.estacionamento.services;

import br.com.ucsal.estacionamento.dto.PlacaMensalistaDTO;
import br.com.ucsal.estacionamento.dto.RegistrarMensalistaDTO;
import br.com.ucsal.estacionamento.dto.TrazerClientesMensalistasEstacionados;
import br.com.ucsal.estacionamento.entity.ClientesMensalistas;
import br.com.ucsal.estacionamento.entity.PlacasMensalistas;
import br.com.ucsal.estacionamento.entity.Vaga;
import br.com.ucsal.estacionamento.repositories.ClienteMensalistaRepository;
import br.com.ucsal.estacionamento.repositories.PlacasMensalistasRepository;
import br.com.ucsal.estacionamento.repositories.TabelaPrecoRepository;
import br.com.ucsal.estacionamento.repositories.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ClienteMensalistaService {


    @Autowired
    private TabelaPrecoRepository tabelaPrecoRepository;
    @Autowired
    private VagaRepository vagaRepository;
    @Autowired
    private ClienteMensalistaRepository clienteMensalistaRepository;
    @Autowired
    private PlacasMensalistasRepository placasMensalistasRepository;

    @Transactional
    public void registrarEntrada(String cpf) {
        ClientesMensalistas clienteMensalista = clienteMensalistaRepository.capturarPorCpf(cpf).orElseThrow(() -> new RuntimeException("n achou"));
        Long vagasLivresMensalista = vagaRepository.quantidadeVagasMensalistasLivre();
        if (vagasLivresMensalista == 0) {
            Long vagasLivresHorista = vagaRepository.quantidadeVagasHoristasLivre();
            if (vagasLivresHorista == 0) {
                throw new RuntimeException("NAO TEM MAIS VAGA NEM HORISTA E NEM MENSALISTA");
            } else {
                PlacasMensalistas veiculoMensalistaSelecionado = placasMensalistasRepository.selecionaUmVeiculoMensalistaAleartorio(clienteMensalista.getId());
                veiculoMensalistaSelecionado.setEntrada(LocalDateTime.now());
                Vaga vagaSelecionada = vagaRepository.selecionaUmaVagaHorista();
                vagaSelecionada.setOcupada(Boolean.TRUE);
                vagaSelecionada.setPlaca(veiculoMensalistaSelecionado.getPlaca());
                placasMensalistasRepository.save(veiculoMensalistaSelecionado);
                vagaRepository.save(vagaSelecionada);
            }
        } else {
            PlacasMensalistas veiculoMensalistaSelecionado = placasMensalistasRepository.selecionaUmVeiculoMensalistaAleartorio(clienteMensalista.getId());
            veiculoMensalistaSelecionado.setEntrada(LocalDateTime.now());
            Vaga vagaSelecionada = vagaRepository.selecionaUmaVagaMensalista();
            vagaSelecionada.setOcupada(Boolean.TRUE);
            vagaSelecionada.setPlaca(veiculoMensalistaSelecionado.getPlaca());
            placasMensalistasRepository.save(veiculoMensalistaSelecionado);
            vagaRepository.save(vagaSelecionada);
        }
    }

    @Transactional
    public void criarMensalista(RegistrarMensalistaDTO registrarMensalistaDTO) {
        List<PlacasMensalistas> placasMensalistas = new ArrayList<>();
        ClientesMensalistas clientesMensalista = new ClientesMensalistas();
        clientesMensalista.setNome(registrarMensalistaDTO.getNome());
        clientesMensalista.setCpf(registrarMensalistaDTO.getCpf());
        clientesMensalista.setTel(registrarMensalistaDTO.getTel());
        for (PlacaMensalistaDTO placa : registrarMensalistaDTO.getPlacas()) {
            placasMensalistas.add(
                    new PlacasMensalistas(placa.getPlaca(), null, null, clientesMensalista)
            );
        }
        clientesMensalista.setPlacas(placasMensalistas);
        clienteMensalistaRepository.save(clientesMensalista);
    }


    @Transactional
    public void registrarSaida(String cpf) {
        ClientesMensalistas clienteMensalista = clienteMensalistaRepository.capturarPorCpf(cpf).orElseThrow(() -> new RuntimeException("n achou"));
        clienteMensalista.getPlacas().forEach(mensalista -> {
            Vaga vagaOcupadaPeloMensalista = vagaRepository.capturaVagaPelaPlaca(mensalista.getPlaca());
            if (vagaOcupadaPeloMensalista != null) {
                vagaOcupadaPeloMensalista.setOcupada(Boolean.FALSE);
                vagaOcupadaPeloMensalista.setPlaca(null);
                PlacasMensalistas placaMensalistaSelecionada = placasMensalistasRepository.selecionaVeiculoPeloIdMensalistaEPlaca(clienteMensalista.getId(), mensalista.getPlaca());
                placaMensalistaSelecionada.setSaida(LocalDateTime.now());
                vagaRepository.save(vagaOcupadaPeloMensalista);
                placasMensalistasRepository.save(placaMensalistaSelecionada);
            }
        });
    }

    @Transactional(readOnly = true)
    public List<TrazerClientesMensalistasEstacionados> trazerMensalistasEstacionados() {
        List<TrazerClientesMensalistasEstacionados> mensalistasEstacionados = new ArrayList<>();
        List<Object[]> resultado = vagaRepository.capturaMensalistasEstacionados();
        for (Object[] linha : resultado) {
            TrazerClientesMensalistasEstacionados trazerClientesMensalistaEstacionados = new TrazerClientesMensalistasEstacionados();
            trazerClientesMensalistaEstacionados.setId((Long) linha[0]);
            trazerClientesMensalistaEstacionados.setPlaca((String) linha[1]);
            trazerClientesMensalistaEstacionados.setDataHoraEntrada((Date) linha[2]);
            trazerClientesMensalistaEstacionados.setCpf((String) linha[3]);
            mensalistasEstacionados.add(trazerClientesMensalistaEstacionados);
        }
        return mensalistasEstacionados;
    }
}
