package br.com.zup.edu.edubank.conta;

import java.math.BigDecimal;

public class ConsultarSaldoResponse {
    private String agencia;
    private String conta;
    private BigDecimal saldo;

    public ConsultarSaldoResponse(Conta conta) {
        this.agencia= conta.getAgencia();
        this.conta= conta.getNumeroDaConta();
        this.saldo=conta.getSaldo();
    }

    @Deprecated
    public ConsultarSaldoResponse() {
    }

    public String getAgencia() {
        return agencia;
    }

    public String getConta() {
        return conta;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
}
