package br.com.zup.edu.edubank.conta;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static org.springframework.http.HttpStatus.*;

public class ContaRequest {
    @NotBlank
    @Email
    private String email;

    @CPF
    @NotBlank
    private String cpf;

    @NotBlank
    private String nome;

    @Length(min = 6, max = 6, message = "deve conter 6 digitos")
    @Pattern(regexp = "[0-9]*", message = "os digitos devem ser caracteres numericos")
    @NotBlank
    private String numeroDaConta;

    @Length(min = 4, max = 4, message = "deve conter 4 digitos")
    @Pattern(regexp = "[0-9]*", message = "os digitos devem ser caracteres numericos")
    @NotBlank
    private String agencia;


    public ContaRequest(String email, String cpf, String nome, String numeroDaConta, String agencia) {
        this.email = email;
        this.cpf = cpf;
        this.nome = nome;
        this.numeroDaConta = numeroDaConta;
        this.agencia = agencia;
    }

    /**
     * @construtor para uso exclusivo de Jackson
     */


    @Deprecated
    public ContaRequest() {
    }


    public Conta paraConta(ContaRepository repository) {

        if (repository.existsByEmail(email)) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, "Já existe cadastro de conta para este email.");
        }

        if (repository.existsByCpf(cpf)) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, "Já existe cadastro de conta para este cpf.");
        }

        return new Conta(agencia, numeroDaConta, cpf, email, nome);
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getNumeroDaConta() {
        return numeroDaConta;
    }

    public String getAgencia() {
        return agencia;
    }
}
