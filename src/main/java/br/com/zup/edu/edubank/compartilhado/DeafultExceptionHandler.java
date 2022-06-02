package br.com.zup.edu.edubank.compartilhado;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.*;
import static org.springframework.http.ResponseEntity.badRequest;

@RestControllerAdvice
public class DeafultExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        List<String> response = fieldErrors.stream().map(
                        erro -> format("O Campo %s %s", erro.getField(), erro.getDefaultMessage())
                )
                .collect(Collectors.toList());

        return badRequest().body(response);
    }
}
