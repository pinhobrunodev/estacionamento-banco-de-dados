package br.com.ucsal.estacionamento.repositories;

import br.com.ucsal.estacionamento.entity.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Objects;

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

    @Query(value = """
            WITH ocupadoHorista AS (
                SELECT 'ocupadoHorista' AS TIPO, V.ID, V.PLACA,
                    CONCAT(
                        EXTRACT(DAY FROM PH.ENTRADA), '/',
                        EXTRACT(MONTH FROM PH.ENTRADA), '/',
                        EXTRACT(YEAR FROM PH.ENTRADA), ' ',
                        EXTRACT(HOUR FROM PH.ENTRADA), ':',
                        EXTRACT(MINUTE FROM PH.ENTRADA), ':',
                        EXTRACT(SECOND FROM PH.ENTRADA)
                    ) AS formatted_entrada
                FROM VAGA V\s
                INNER JOIN PLACAS_HORISTAS PH ON PH.PLACA = V.PLACA\s
                WHERE V.OCUPADA = TRUE AND V.MENSALISTA = FALSE
            ),
            ocupadoMensalista AS (
                SELECT 'ocupadoMensalista' AS TIPO, V.ID, V.PLACA,
                    CONCAT(
                        EXTRACT(DAY FROM PM.ENTRADA), '/',
                        EXTRACT(MONTH FROM PM.ENTRADA), '/',
                        EXTRACT(YEAR FROM PM.ENTRADA), ' ',
                        EXTRACT(HOUR FROM PM.ENTRADA), ':',
                        EXTRACT(MINUTE FROM PM.ENTRADA), ':',
                        EXTRACT(SECOND FROM PM.ENTRADA)
                    ) AS formatted_entrada
                FROM VAGA V\s
                INNER JOIN PLACAS_MENSALISTAS PM ON PM.PLACA = V.PLACA\s
                WHERE V.OCUPADA = TRUE AND V.MENSALISTA = TRUE
            )
            SELECT * FROM ocupadoHorista\s
            UNION ALL
            SELECT * FROM ocupadoMensalista""", nativeQuery = true)
    List<Object[]> capturaStatusDasVagas();
}
