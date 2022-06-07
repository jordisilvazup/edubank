package br.com.zup.edu.edubank.conta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

import static org.springframework.data.domain.Sort.Direction.ASC;

@RestController
public class ListarTransferenciasController {
    private final TransferenciaRepository repository;

    public ListarTransferenciasController(TransferenciaRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/transferencias/{id}")
    @Transactional
    public ResponseEntity<?> listar(
            @PathVariable Long id,
            @PageableDefault(size = 10, page = 0, sort = "id", direction = ASC) Pageable pageable
    ) {
        Page<TransferenciaResponse> response = repository.findAllTransferenciaByOrigemIdOrDestinoId(id, pageable)
                .map(TransferenciaResponse::new);
        return ResponseEntity.ok(response);
    }
}
