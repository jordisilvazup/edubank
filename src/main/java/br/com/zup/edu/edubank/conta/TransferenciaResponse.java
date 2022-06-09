package br.com.zup.edu.edubank.conta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransferenciaResponse {
    private Long id;
    private ContaResponse origem;
    private ContaResponse destino;
    private BigDecimal valor;
    private LocalDateTime realizadoEm;

    public TransferenciaResponse(Transferencia transferencia) {
        this.id= transferencia.getId();
        this.origem= new ContaResponse(transferencia.getOrigem());
        this.destino= new ContaResponse(transferencia.getDestino());
        this.valor=transferencia.getValor();
        this.realizadoEm=transferencia.getCriadoEm();
    }

    @Deprecated
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

    @Override
    public String toString() {
        return "TransferenciaResponse{" +
                "id=" + id +
                ", origem=" + origem +
                ", destino=" + destino +
                ", valor=" + valor +
                ", realizadoEm=" + realizadoEm +
                '}';
    }


}
