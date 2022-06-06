package br.com.zup.edu.edubank.conta;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
public class TransferirSaldoController {
    private final TransferenciaRepository transferenciaRepository;
    private final ContaRepository contaRepository;

    public TransferirSaldoController(TransferenciaRepository transferenciaRepository, ContaRepository contaRepository) {
        this.transferenciaRepository = transferenciaRepository;
        this.contaRepository = contaRepository;
    }

    @PostMapping("/transferencias")
    @Transactional
    public ResponseEntity<?> transferir(
            @RequestBody @Valid TransferenciaRequest request, UriComponentsBuilder uriComponentsBuilder
    ) {

        Transferencia transferencia = request.paraTransferencia(contaRepository);

        transferencia.transferir();

        transferenciaRepository.save(transferencia);


        URI location = uriComponentsBuilder.path("/transferencias/{id}")
                .buildAndExpand(transferencia.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
