package br.com.zup.edu.edubank.conta;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TransferenciaRepositoryTest {
    @Autowired
    private TransferenciaRepository transferenciaRepository;
    @Autowired
    private ContaRepository contaRepository;
    private Conta contaUm;
    private Conta contaDois;
    private Conta contaTres;


    @BeforeEach
    void setUp() {
        transferenciaRepository.deleteAll();
        contaRepository.deleteAll();
        this.contaUm = new Conta("0001", "000256", "12345678909", "jordi@zup.com.br", "jordi");
        this.contaDois = new Conta("0001", "000257", "71700673076", "rafael@zup.com.br", "rafael");
        this.contaTres = new Conta("0001", "000258", "60672020009", "yuri@zup.com.br", "yuri");
        BigDecimal saldo = new BigDecimal("50");
        contaUm.depositar(saldo);
        contaDois.depositar(saldo);
        contaTres.depositar(saldo);
        contaRepository.saveAll(List.of(contaUm, contaDois, contaTres));
    }

    @Test
    @DisplayName("deve retornar todas transferencias enviadas e recebidas de uma conta")
    void name() {

        Transferencia contaUmParaDois15 = new Transferencia(contaUm, contaDois, new BigDecimal("15").setScale(2));
        Transferencia contaUmParaDois10 = new Transferencia(contaUm, contaDois, new BigDecimal("10").setScale(2));
        Transferencia contaTresParaUm10 = new Transferencia(contaTres, contaUm, new BigDecimal("10").setScale(2));
        Transferencia contaTresParaDois5 = new Transferencia(contaTres, contaDois, new BigDecimal("5").setScale(2));
        Transferencia contaDoisParaTres11 = new Transferencia(contaTres, contaDois, new BigDecimal("11").setScale(2));

        List<Transferencia> transferencias = List.of(
                contaUmParaDois15,
                contaUmParaDois10,
                contaTresParaUm10,
                contaTresParaDois5,
                contaDoisParaTres11
        );

        transferenciaRepository.saveAll(transferencias);

        Page<Transferencia> response = transferenciaRepository
                .findAllTransferenciaByOrigemIdOrDestinoId(contaUm.getId(), Pageable.unpaged());

        assertThat(response.getTotalElements())
                .isEqualTo(3);

        assertThat(response.getTotalPages())
                .isEqualTo(1);


        assertThat(response.get())
                .hasSize(3)
                .extracting(Transferencia::getId, Transferencia::getValor)
                .contains(
                        new Tuple(contaUmParaDois10.getId(), contaUmParaDois10.getValor()),
                        new Tuple(contaUmParaDois15.getId(), contaUmParaDois15.getValor()),
                        new Tuple(contaTresParaUm10.getId(), contaTresParaUm10.getValor())
                );
    }    
    
    
    @Test
    @DisplayName("deve retornar todas transferencias enviadas conta")
    void test() {

        Transferencia contaUmParaDois15 = new Transferencia(contaUm, contaDois, new BigDecimal("15").setScale(2));
        Transferencia contaUmParaTres10 = new Transferencia(contaUm, contaTres, new BigDecimal("10").setScale(2));
        Transferencia contaTresParaDois10 = new Transferencia(contaTres, contaDois, new BigDecimal("10").setScale(2));
        Transferencia contaDoisParaTres5 = new Transferencia(contaDois, contaTres, new BigDecimal("5").setScale(2));

        List<Transferencia> transferencias = List.of(
                contaUmParaDois15,
                contaUmParaTres10,
                contaTresParaDois10,
                contaDoisParaTres5
        );

        transferenciaRepository.saveAll(transferencias);

        Page<Transferencia> response = transferenciaRepository
                .findAllTransferenciaByOrigemIdOrDestinoId(contaUm.getId(), Pageable.unpaged());

        assertThat(response.getTotalElements())
                .isEqualTo(2);

        assertThat(response.getTotalPages())
                .isEqualTo(1);


        assertThat(response.get())
                .hasSize(2)
                .extracting(Transferencia::getId, Transferencia::getValor)
                .contains(
                        new Tuple(contaUmParaDois15.getId(), contaUmParaDois15.getValor()),
                        new Tuple(contaUmParaTres10.getId(), contaUmParaTres10.getValor())
                );
    }

    @Test
    @DisplayName("deve retornar todas transferencias recebidas conta")
    void test1() {

        Transferencia contaUmParaDois15 = new Transferencia(contaUm, contaDois, new BigDecimal("15").setScale(2));
        Transferencia contaUmParaTres10 = new Transferencia(contaUm, contaTres, new BigDecimal("10").setScale(2));
        Transferencia contaTresParaDois10 = new Transferencia(contaTres, contaDois, new BigDecimal("10").setScale(2));

        List<Transferencia> transferencias = List.of(
                contaUmParaDois15,
                contaUmParaTres10,
                contaTresParaDois10
        );

        transferenciaRepository.saveAll(transferencias);

        Page<Transferencia> response = transferenciaRepository
                .findAllTransferenciaByOrigemIdOrDestinoId(contaDois.getId(), Pageable.unpaged());

        assertThat(response.getTotalElements())
                .isEqualTo(2);

        assertThat(response.getTotalPages())
                .isEqualTo(1);

        assertThat(response.get())
                .hasSize(2)
                .extracting(Transferencia::getId, Transferencia::getValor)
                .contains(
                        new Tuple(contaUmParaDois15.getId(), contaUmParaDois15.getValor()),
                        new Tuple(contaTresParaDois10.getId(), contaTresParaDois10.getValor())
                );
    }
}