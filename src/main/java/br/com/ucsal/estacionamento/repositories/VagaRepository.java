package br.com.ucsal.estacionamento.repositories;

import br.com.ucsal.estacionamento.entity.TabelaPreco;
import br.com.ucsal.estacionamento.entity.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VagaRepository extends JpaRepository<Vaga, Long> {

    @Query(value = "SELECT * FROM VAGA WHERE mensalista = FALSE AND ocupada = FALSE ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Vaga selecionaUmaVagaHorista();

    @Query(value = "SELECT * FROM VAGA WHERE mensalista = TRUE AND ocupada = FALSE ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Vaga selecionaUmaVagaMensalista();

    @Query(value = "SELECT COUNT(*)  FROM VAGA  WHERE mensalista = FALSE AND ocupada = FALSE", nativeQuery = true)
    Long quantidadeVagasHoristasLivre();

    @Query(value = "SELECT COUNT(*)  FROM VAGA  WHERE mensalista = TRUE AND ocupada = FALSE", nativeQuery = true)
    Long quantidadeVagasMensalistasLivre();
    @Query(value = "SELECT *  FROM VAGA  WHERE placa = ?1", nativeQuery = true)
    Vaga capturaVagaPelaPlaca(String placa);
}
