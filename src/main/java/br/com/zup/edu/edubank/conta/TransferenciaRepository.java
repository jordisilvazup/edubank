package br.com.zup.edu.edubank.conta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
    @Query("select t from Transferencia t where (t.origem.id =:id or t.destino.id=:id) ")
    Page<Transferencia> findAllTransferenciaByOrigemIdOrDestinoId(@Param("id") Long id, Pageable pagincao);
}
