package br.com.zup.edu.edubank.conta;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

import static org.springframework.http.HttpStatus.*;

@RestController
public class ConsultarSaldoController {
    private final ContaRepository repository;

    public ConsultarSaldoController(ContaRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/contas/{id}")
    @Transactional
    public ResponseEntity<?> consultar(@PathVariable Long id) {
        Conta conta = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Conta não cadastrada no sistema."));

        return ResponseEntity.ok(new ConsultarSaldoResponse(conta));
    }
}
