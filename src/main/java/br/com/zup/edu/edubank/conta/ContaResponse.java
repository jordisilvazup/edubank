package br.com.zup.edu.edubank.conta;

public class ContaResponse {
    private String agencia;
    private String numeroConta;
    private String titular;

    public ContaResponse(Conta conta) {
        this.agencia=conta.getAgencia();
        this.numeroConta=conta.getNumeroDaConta();
        this.titular=conta.getNome();
    }

    public ContaResponse() {
    }

    public String getAgencia() {
        return agencia;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public String getTitular() {
        return titular;
    }
}
