package br.com.zup.edu.edubank.conta;


import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.math.BigDecimal;

@Entity
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

    public void sacar(BigDecimal valor) {
        if (this.saldo.compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("não é possivel sacar um valor maior do que o saldo atual da conta.");
        }
        this.saldo = saldo.subtract(valor);
    }

    public void depositar(BigDecimal valor) {
        this.saldo = saldo.add(valor);
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
}
