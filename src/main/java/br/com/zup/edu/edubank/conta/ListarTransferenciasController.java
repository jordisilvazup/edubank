package br.com.zup.edu.edubank.conta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;



import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.http.HttpStatus.*;

@RestController
public class ListarTransferenciasController {
    private final TransferenciaRepository repository;
    private final ContaRepository contaRepository;

    public ListarTransferenciasController(TransferenciaRepository repository, ContaRepository contaRepository) {
        this.repository = repository;
        this.contaRepository = contaRepository;
    }

    @GetMapping("/contas/{id}/transferencias")
    @Transactional(readOnly = true)
    public ResponseEntity<?> listar(
            @PathVariable Long id,
            @PageableDefault(size = 10, page = 0, sort = "id", direction = ASC) Pageable pageable
    ) {
        if(!contaRepository.existsById(id)){
            throw  new ResponseStatusException(NOT_FOUND,"Conta não cadastrada no sistema.");
        }

        Page<TransferenciaResponse> response = repository.findAllTransferenciaByOrigemIdOrDestinoId(id, pageable)
                .map(TransferenciaResponse::new);

        return ResponseEntity.ok(response);
    }
}
