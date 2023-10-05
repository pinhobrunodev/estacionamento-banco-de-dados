package br.com.ucsal.estacionamento.repositories;

import br.com.ucsal.estacionamento.entity.ClientesMensalistas;
import br.com.ucsal.estacionamento.entity.PlacasHoristas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClienteMensalistaRepository extends JpaRepository<ClientesMensalistas,String> {

    @Query(value = "SELECT * FROM CLIENTES_MENSALISTAS WHERE cpf = ?1",nativeQuery = true)
    Optional<ClientesMensalistas> capturarPorCpf(String cpf);
}
