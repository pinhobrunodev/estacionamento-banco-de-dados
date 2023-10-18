package br.com.ucsal.estacionamento.repositories;

import br.com.ucsal.estacionamento.entity.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VagaRepository extends JpaRepository<Vaga, Long> {

    @Query(value = "SELECT * FROM VAGA WHERE mensalista = FALSE AND ocupada = FALSE ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Vaga selecionaUmaVagaHorista();

    @Query(value = "SELECT * FROM VAGA WHERE mensalista = TRUE AND ocupada = FALSE ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Vaga selecionaUmaVagaMensalista();

    @Query(value = "SELECT COUNT(*)  FROM VAGA  WHERE mensalista = FALSE AND ocupada = FALSE", nativeQuery = true)
    Long quantidadeVagasHoristasLivre();

    @Query(value = "SELECT COUNT(*)  FROM VAGA  WHERE mensalista = TRUE AND ocupada = FALSE", nativeQuery = true)
    Long quantidadeVagasMensalistasLivre();

    @Query(value = "SELECT COUNT(*)  FROM VAGA  WHERE mensalista = FALSE AND ocupada = TRUE", nativeQuery = true)
    Long quantidadeVagasHoristasOcupado();

    @Query(value = "SELECT COUNT(*)  FROM VAGA  WHERE mensalista = TRUE AND ocupada = TRUE", nativeQuery = true)
    Long quantidadeVagasMensalistasOcupado();


    @Query(value = "SELECT *  FROM VAGA  WHERE placa = ?1", nativeQuery = true)
    Vaga capturaVagaPelaPlaca(String placa);


    @Query(value = "SELECT V.ID,V.PLACA,PH.ENTRADA FROM VAGA V INNER JOIN PLACAS_HORISTAS PH ON PH.PLACA = V.PLACA WHERE V.MENSALISTA = FALSE AND V.OCUPADA = TRUE", nativeQuery = true)
    List<Object[]> capturaHoristasEstacionados();

    @Query(value = "SELECT V.ID,V.PLACA,PM.ENTRADA,CM.CPF FROM VAGA V INNER JOIN PLACAS_MENSALISTAS PM ON PM.PLACA = V.PLACA INNER JOIN CLIENTES_MENSALISTAS CM ON CM.ID = PM.CLIENTE_MENSALISTA_ID WHERE V.MENSALISTA = TRUE  AND V.OCUPADA = TRUE", nativeQuery = true)
    List<Object[]> capturaMensalistasEstacionados();

    @Query(value = "SELECT V.ID,PM.PLACA,PM.ENTRADA FROM VAGA V INNER JOIN PLACAS_MENSALISTAS PM ON PM.PLACA = V.PLACA WHERE V.MENSALISTA = FALSE", nativeQuery = true)
    List<Object[]> capturaMensalistasEmVagasDeHorista();
}
