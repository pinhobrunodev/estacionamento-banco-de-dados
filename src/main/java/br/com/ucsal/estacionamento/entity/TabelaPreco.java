package br.com.ucsal.estacionamento.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "TABELA_PRECO")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TabelaPreco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double preco;
    private Integer apartirDeQuantasHoras;
    private Boolean mensalista;
    private LocalDate dataValidade;
}
