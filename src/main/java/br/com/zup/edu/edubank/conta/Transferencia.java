package br.com.zup.edu.edubank.conta;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

    public void transferir(){
        origem.sacar(valor);
        destino.depositar(valor);
    }


    public Long getId() {
        return id;
    }
}
