package br.com.zup.edu.edubank.conta;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(String msg) {
        super(msg);
    }
}
