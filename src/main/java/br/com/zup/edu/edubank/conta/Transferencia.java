package br.com.zup.edu.edubank.conta;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transferencia_sequence")
    @GenericGenerator(
            name = "transferencia_sequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "transferencia_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "conta_origem_id")
    private Conta origem;

    @ManyToOne(optional = false)
    @JoinColumn(name = "conta_destino_id")
    private Conta destino;

    @Column(nullable = false)
    @Positive
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    public Transferencia(Conta origem, Conta destino, BigDecimal valor) {
        this.origem = origem;
        this.destino = destino;
        this.valor = valor;
    }

    @Deprecated
    public Transferencia() {
    }

    public void transferir() {
        origem.sacar(valor);
        destino.depositar(valor);
    }


    public Long getId() {
        return id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Conta getOrigem() {
        return origem;
    }

    public Conta getDestino() {
        return destino;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
