package br.com.ucsal.estacionamento.repositories;

import br.com.ucsal.estacionamento.entity.PlacasHoristas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteHoristaRepository extends JpaRepository<PlacasHoristas,String> {



}
