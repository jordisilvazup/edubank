package br.com.zup.edu.edubank.conta;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ContaTest {
    private Conta conta;


    @BeforeEach
    void setUp() {
        this.conta = new Conta(
                "0012", "122334", "180.278.000-99", "jordi@email.com", "Jordi H"
        );
    }

    @ParameterizedTest(
            name = "{index}=>  valorDeposito={0}"
    )
    @MethodSource("depositoValidoProvider")
    @DisplayName("deve deposita um saldo")
    void test(BigDecimal valorDeposito) {
        conta.depositar(valorDeposito);

        assertEquals(
                valorDeposito,
                conta.getSaldo(),
                String.format("o saldo apos o deposito deveria ser %f", valorDeposito)
        );
    }

    @ParameterizedTest(
            name = "{index}=>  valorDeposito={0}"
    )
    @MethodSource("depositoInvalidoProvider")
    @DisplayName("nao é possivel fazer deposito de valores abaixo de zero")
    void test1(BigDecimal valorDeposito) {
        ValorParaDepositoInvalidoException valorParaDepositoInvalidoException = assertThrows(ValorParaDepositoInvalidoException.class, () -> {
            conta.depositar(valorDeposito);
        });

        assertEquals(
                "Não é permitido deposito de valores abaixo de zero"
                , valorParaDepositoInvalidoException.getMessage()
        );
    }

    @ParameterizedTest(
            name = "{index}=>  valorSaque={0}"
    )
    @MethodSource("saqueInvalidoProvider")
    @DisplayName("nao é possivel fazer saque de valores maiores que o saldo da conta")
    void test2(BigDecimal valorSaque) {
        SaldoInsuficienteException valorParaDepositoInvalidoException = assertThrows(SaldoInsuficienteException.class, () -> {
            conta.sacar(valorSaque);
        });

        assertEquals(
                "não é possivel sacar um valor maior do que o saldo atual da conta."
                , valorParaDepositoInvalidoException.getMessage()
        );
    }

    @ParameterizedTest(
            name = "{index}=>  deposito={0}, saque={1}, resultado={2}"
    )
    @MethodSource("saqueValidoProvider")
    @DisplayName("nao é possivel fazer saque de valores maiores que o saldo da conta")
    void test2(BigDecimal deposito,BigDecimal saque,BigDecimal resultado) {
        conta.depositar(deposito);
        conta.sacar(saque);

        assertEquals(
            resultado,
            conta.getSaldo(),
            String.format("o saldo deveria corresponder a %f",resultado)
        );
    }


    private static Stream<Arguments> saqueValidoProvider() {
        return Stream.of(
                Arguments.of(new BigDecimal("50"), new BigDecimal("15"), new BigDecimal("35")),
                Arguments.of(new BigDecimal("20"), new BigDecimal("15"), new BigDecimal("5")),
                Arguments.of(new BigDecimal("10"), new BigDecimal("1"), new BigDecimal("9"))
        );
    }

    private static Stream<Arguments> saqueInvalidoProvider() {
        return Stream.of(
                Arguments.of(new BigDecimal("50")),
                Arguments.of(new BigDecimal("20")),
                Arguments.of(new BigDecimal("10"))
        );
    }

    private static Stream<Arguments> depositoInvalidoProvider() {
        return Stream.of(
                Arguments.of(new BigDecimal("-1")),
                Arguments.of(BigDecimal.ZERO)
        );
    }

    private static Stream<Arguments> depositoValidoProvider() {
        return Stream.of(
                Arguments.of(new BigDecimal("2")),
                Arguments.of(BigDecimal.ONE)
        );
    }
}