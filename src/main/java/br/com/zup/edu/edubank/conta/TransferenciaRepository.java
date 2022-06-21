package br.com.zup.edu.edubank.conta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
    @Query(
            value = "select t from Transferencia t join fetch t.origem join fetch t.destino" +
                    " where (t.origem.id =:id or t.destino.id=:id)",
            countQuery = "select count(t) from Transferencia t join t.origem join t.destino" +
                    " where (t.origem.id =:id or t.destino.id=:id)"
    )
    Page<Transferencia> findAllTransferenciaByOrigemIdOrDestinoId(@Param("id") Long id,  Pageable paginacao);

}
