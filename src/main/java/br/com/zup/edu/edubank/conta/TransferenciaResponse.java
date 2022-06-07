package br.com.zup.edu.edubank.conta;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransferenciaResponse {
    private Long id;
    private ContaResponse origem;
    private ContaResponse destino;
    private BigDecimal valor;
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime realizadoEm;

    public TransferenciaResponse(Transferencia transferencia) {
        this.id= transferencia.getId();
        this.origem= new ContaResponse(transferencia.getOrigem());
        this.destino= new ContaResponse(transferencia.getDestino());
        this.valor=transferencia.getValor();
        this.realizadoEm=transferencia.getCriadoEm();
    }

    public TransferenciaResponse() {
    }

    public Long getId() {
        return id;
    }

    public ContaResponse getOrigem() {
        return origem;
    }

    public ContaResponse getDestino() {
        return destino;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getRealizadoEm() {
        return realizadoEm;
    }
}
