package br.com.ucsal.estacionamento.repositories;

import br.com.ucsal.estacionamento.entity.TabelaPreco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TabelaPrecoRepository extends JpaRepository<TabelaPreco,Long> {
}
