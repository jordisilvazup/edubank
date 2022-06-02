package br.com.zup.edu.edubank.conta;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.ResponseEntity.created;

@RestController
public class CadastrarContaController {
    private final ContaRepository repository;

    public CadastrarContaController(ContaRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/contas")
    @Transactional
    public ResponseEntity<?> cadastrar(
            @RequestBody @Valid ContaRequest request,
            UriComponentsBuilder uriComponentsBuilder
    ) {

        Conta conta = request.paraConta(repository);

        repository.save(conta);

        URI location = uriComponentsBuilder.path("/contas/{id}")
                .buildAndExpand(conta.getId())
                .toUri();

        return created(location).build();
    }
}
