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
}
