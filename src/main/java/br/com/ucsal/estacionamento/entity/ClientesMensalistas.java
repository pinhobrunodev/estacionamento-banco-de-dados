package br.com.ucsal.estacionamento.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CLIENTES_MENSALISTAS")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientesMensalistas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cpf;
    private String tel;
    @OneToMany(mappedBy = "clienteMensalista")
    private List<PlacasMensalistas> placas = new ArrayList<>();
}
