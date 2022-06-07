package br.com.zup.edu.edubank.conta;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ResponseStatus(UNPROCESSABLE_ENTITY)
public class ValorParaDepositoInvalidoException extends RuntimeException {
    public ValorParaDepositoInvalidoException(String msg) {
        super(msg);
    }
}
