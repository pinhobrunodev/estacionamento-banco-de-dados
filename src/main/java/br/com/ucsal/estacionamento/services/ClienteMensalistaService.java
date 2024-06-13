package br.com.ucsal.estacionamento.services;

import br.com.ucsal.estacionamento.dto.PlacaMensalistaDTO;
import br.com.ucsal.estacionamento.dto.RegistrarMensalistaDTO;
import br.com.ucsal.estacionamento.dto.TrazerClientesMensalistasCriadosDTO;
import br.com.ucsal.estacionamento.dto.TrazerClientesMensalistasEstacionados;
import br.com.ucsal.estacionamento.entity.ClientesMensalistas;
import br.com.ucsal.estacionamento.entity.PlacasHoristas;
import br.com.ucsal.estacionamento.entity.PlacasMensalistas;
import br.com.ucsal.estacionamento.entity.Vaga;
import br.com.ucsal.estacionamento.exception.BusinessException;
import br.com.ucsal.estacionamento.repositories.ClienteMensalistaRepository;
import br.com.ucsal.estacionamento.repositories.PlacasMensalistasRepository;
import br.com.ucsal.estacionamento.repositories.TabelaPrecoRepository;
import br.com.ucsal.estacionamento.repositories.VagaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
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
        var clienteMensalista = clienteMensalistaRepository.capturarPorCpf(cpf);
        if (clienteMensalista.isEmpty()) {
            throw new BusinessException("CPF inexistente");
        }

        var placasDoMensalistaEscolhido = placasMensalistasRepository.trazerPlacasByClienteMensalistaId(clienteMensalista.get().getId());
        Long vagasLivresMensalista = vagaRepository.quantidadeVagasMensalistasLivre();

        if (vagasLivresMensalista == 0) {
            Long vagasLivresHorista = vagaRepository.quantidadeVagasHoristasLivre();
            if (vagasLivresHorista == 0) {
                throw new BusinessException("Sem vagas disponiveis.");
            } else {
                PlacasMensalistas veiculoMensalistaSelecionado = selecionarVeiculoDisponivel(clienteMensalista.get().getId(), placasDoMensalistaEscolhido);
                if (veiculoMensalistaSelecionado == null) {
                    throw new BusinessException("Mensalista ja estacionou todos os veiculos.");
                }
                estacionarVeiculo(veiculoMensalistaSelecionado, false);
            }
        } else {
            PlacasMensalistas veiculoMensalistaSelecionado = selecionarVeiculoDisponivel(clienteMensalista.get().getId(), placasDoMensalistaEscolhido);
            if (veiculoMensalistaSelecionado == null) {
                throw new BusinessException("Mensalista ja estacionou todos os veiculos.");
            }
            estacionarVeiculo(veiculoMensalistaSelecionado, true);
        }
    }

    private PlacasMensalistas selecionarVeiculoDisponivel(Long clienteMensalistaId, List<PlacasMensalistas> placasDoMensalista) {
        for (PlacasMensalistas placa : placasDoMensalista) {
            var vagaComPlaca = vagaRepository.capturaVagaPelaPlaca(placa.getPlaca());
            if (vagaComPlaca == null || !vagaComPlaca.getOcupada()) {
                return placa;
            }
        }
        return null;
    }

    private void estacionarVeiculo(PlacasMensalistas veiculoMensalistaSelecionado, boolean isMensalista) {
        veiculoMensalistaSelecionado.setEntrada(LocalDateTime.now());
        Vaga vagaSelecionada;
        if (isMensalista) {
            vagaSelecionada = vagaRepository.selecionaUmaVagaMensalista();
        } else {
            vagaSelecionada = vagaRepository.selecionaUmaVagaHorista();
        }
        vagaSelecionada.setOcupada(Boolean.TRUE);
        vagaSelecionada.setPlaca(veiculoMensalistaSelecionado.getPlaca());
        placasMensalistasRepository.save(veiculoMensalistaSelecionado);
        vagaRepository.save(vagaSelecionada);
    }


    @Transactional
    public void criarMensalista(RegistrarMensalistaDTO registrarMensalistaDTO) {
        var resp = clienteMensalistaRepository.capturarPorCpf(registrarMensalistaDTO.getCpf());
        if (resp.isPresent()) {
            throw new BusinessException("CPF ja existe");
        }
        registrarMensalistaDTO.getPlacas().forEach(placaMensalistaDTO -> {
            var exists = placasMensalistasRepository.existsByPlaca(placaMensalistaDTO.getPlaca());
            if (exists) {
                throw new BusinessException("Placa ja associada com um mensalista");
            }
        });
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
        log.info("Iniciando processo de registrar saída para o CPF: {}", cpf);

        var clientesMensalistas = clienteMensalistaRepository.capturarPorCpf(cpf);
        if (clientesMensalistas.isEmpty()) {
            log.error("CPF inexistente: {}", cpf);
            throw new BusinessException("CPF inexistente");
        }

        PlacasMensalistas placaMensalistaEscolhidaRandomParaSaida = getRandomPlacaEstacionada(clientesMensalistas.get());
        log.info("Placa escolhida para saída: {}", placaMensalistaEscolhidaRandomParaSaida.getPlaca());

        Vaga vagaOcupadaPeloMensalista = vagaRepository.capturaVagaPelaPlaca(placaMensalistaEscolhidaRandomParaSaida.getPlaca());
        if (vagaOcupadaPeloMensalista != null) {
            vagaOcupadaPeloMensalista.setOcupada(Boolean.FALSE);
            vagaOcupadaPeloMensalista.setPlaca(null);

            PlacasMensalistas placaMensalistaSelecionada = placasMensalistasRepository.selecionaVeiculoPeloIdMensalistaEPlaca(clientesMensalistas.get().getId(), placaMensalistaEscolhidaRandomParaSaida.getPlaca());
            placaMensalistaSelecionada.setSaida(LocalDateTime.now());

            vagaRepository.save(vagaOcupadaPeloMensalista);
            placasMensalistasRepository.save(placaMensalistaSelecionada);

            log.info("Saída registrada com sucesso para a placa: {}", placaMensalistaEscolhidaRandomParaSaida.getPlaca());
        } else {
            log.error("Mensalista sem carros estacionados para o CPF: {}", cpf);
            throw new BusinessException("Mensalista sem carros estacionados");
        }
    }

    public PlacasMensalistas getRandomPlacaEstacionada(ClientesMensalistas clientesMensalistas) {
        log.info("Selecionando placa estacionada aleatoriamente.");

        List<PlacasMensalistas> placas = clientesMensalistas.getPlacas();
        List<PlacasMensalistas> placasEstacionadas = placas.stream()
                .filter(placa -> vagaRepository.capturaVagaPelaPlaca(placa.getPlaca()) != null)
                .toList();

        if (placasEstacionadas.isEmpty()) {
            log.error("Nenhuma placa estacionada disponível para o mensalista: {}", clientesMensalistas.getId());
            throw new BusinessException("Nenhuma placa estacionada disponível.");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(placasEstacionadas.size());
        PlacasMensalistas placaEscolhida = placasEstacionadas.get(randomIndex);

        log.info("Placa estacionada selecionada: {}", placaEscolhida.getPlaca());
        return placaEscolhida;
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

    @Transactional(readOnly = true)
    public List<TrazerClientesMensalistasCriadosDTO> trazerMensalistas() {
        return clienteMensalistaRepository.findAll().stream().map(TrazerClientesMensalistasCriadosDTO::new).collect(Collectors.toList());
    }
}
