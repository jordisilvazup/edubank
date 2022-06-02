package br.com.zup.edu.edubank.conta;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}
