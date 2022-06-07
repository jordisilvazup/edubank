package br.com.zup.edu.edubank.compartilhado;

import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.*;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
public class DeafultExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValid(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        List<FieldError> fieldErrors = bindingResult
                .getFieldErrors();



        List<String> response = fieldErrors.stream().map(
                        erro -> format("O Campo %s %s", erro.getField(), erro.getDefaultMessage())
                )
                .collect(Collectors.toList());

        return badRequest().body(response);
    }


    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<?> optmisticLock(ObjectOptimisticLockingFailureException ex) {

        String msg = "Infelizmente ocorreu um erro, tente novamente.";

        return status(409)
                .body(
                        Map.of("mensagem",msg)
                );
    }
}
