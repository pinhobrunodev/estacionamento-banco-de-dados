package br.com.ucsal.estacionamento.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "PLACAS_HORISTAS")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlacasHoristas {
    @Id
    private String placa;
    private LocalDateTime entrada;
    private LocalDateTime saida;
}
