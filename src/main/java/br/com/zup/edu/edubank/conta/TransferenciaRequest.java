package br.com.zup.edu.edubank.conta;

import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

public class TransferenciaRequest {
    @NotNull
    @Positive
    private Long idOrigem;

    @NotNull
    @Positive
    private Long idDestino;

    @Positive
    @NotNull
    private BigDecimal valor;

    public TransferenciaRequest(Long idOrigem, Long idDestino, BigDecimal valor) {
        this.idOrigem = idOrigem;
        this.idDestino = idDestino;
        this.valor = valor;
    }

    /**
     * @construtor de uso exclusivo do hibernate
     */
    @Deprecated
    public TransferenciaRequest() {
    }

    public Transferencia paraTransferencia(ContaRepository contaRepository) {
        Conta origem = contaRepository.findById(idOrigem).orElseThrow(() ->
                new ResponseStatusException(UNPROCESSABLE_ENTITY, "Não existe cadastro de conta para o id de origem"));

        Conta destino = contaRepository.findById(idDestino).orElseThrow(() ->
                new ResponseStatusException(UNPROCESSABLE_ENTITY, "Não existe cadastro de conta para o id de destino"));

        return new Transferencia(origem,destino,valor);
    }

    public Long getIdOrigem() {
        return idOrigem;
    }

    public Long getIdDestino() {
        return idDestino;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
