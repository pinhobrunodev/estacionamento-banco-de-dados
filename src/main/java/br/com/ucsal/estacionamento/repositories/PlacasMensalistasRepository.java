package br.com.ucsal.estacionamento.repositories;

import br.com.ucsal.estacionamento.entity.PlacasMensalistas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlacasMensalistasRepository extends JpaRepository<PlacasMensalistas, String> {
    @Query(value = "SELECT * FROM PLACAS_MENSALISTAS WHERE cliente_mensalista_id = ?1 ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    PlacasMensalistas selecionaUmVeiculoMensalistaAleartorio(Long id);
    @Query(value = "SELECT * FROM PLACAS_MENSALISTAS WHERE cliente_mensalista_id = ?1 AND placa = ?2", nativeQuery = true)
    PlacasMensalistas selecionaVeiculoPeloIdMensalistaEPlaca(Long id,String placa);




}
