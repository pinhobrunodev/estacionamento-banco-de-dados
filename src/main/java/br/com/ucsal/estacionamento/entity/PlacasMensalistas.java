package br.com.ucsal.estacionamento.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "PLACAS_MENSALISTAS")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlacasMensalistas {
    @Id
    private String placa;
    @ManyToOne
    @JoinColumn(name = "cliente_mensalista_id")
    private ClientesMensalistas clienteMensalista;
}
