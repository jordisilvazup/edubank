package br.com.zup.edu.edubank.conta;


import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.math.BigDecimal;

@Entity
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conta_sequence")
    @GenericGenerator(
            name = "conta_sequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "conta_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Column(nullable = false)
    private String agencia;

    @Column(nullable = false)
    private String numeroDaConta;

    @CPF
    @Column(unique = true, nullable = false)
    private String cpf;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private BigDecimal saldo = BigDecimal.ZERO;

    @Version
    private Long versao;

    public Conta(String agencia, String numeroDaConta, String cpf, String email, String nome) {
        this.agencia = agencia;
        this.numeroDaConta = numeroDaConta;
        this.cpf = cpf;
        this.email = email;
        this.nome = nome;
    }

    @Deprecated
    public Conta() {
    }

    public Long getId() {
        return id;
    }

    public String getAgencia() {
        return agencia;
    }

    public String getNumeroDaConta() {
        return numeroDaConta;
    }

    public String getNome() {
        return nome;
    }

    public void sacar(BigDecimal valor) {
        if (this.saldo.compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("não é possivel sacar um valor maior do que o saldo atual da conta.");
        }
        this.saldo = saldo.subtract(valor);
    }

    public void depositar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorParaDepositoInvalidoException("Não é permitido deposito de valores abaixo de zero");
        }
        this.saldo = saldo.add(valor);
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
}
