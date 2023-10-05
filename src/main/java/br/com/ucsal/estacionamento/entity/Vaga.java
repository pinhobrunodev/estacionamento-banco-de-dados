package br.com.ucsal.estacionamento.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "VAGA")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Vaga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean mensalista;
    private Boolean ocupada;
    private String placa;
}
