package br.com.zup.edu.edubank.conta;

import br.com.zup.edu.edubank.util.BaseIntegrationTest;
import br.com.zup.edu.edubank.util.JsonUtilsTest;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransferirSaldoControllerTest extends BaseIntegrationTest {
    @Autowired
    private JsonUtilsTest jsonUtils;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    @Autowired
    private ContaRepository contaRepository;

    private Conta contaUm;
    private Conta contaDois;

    @BeforeEach
    void setUp() {
        transferenciaRepository.deleteAll();
        contaRepository.deleteAll();
        this.contaUm = new Conta("0001", "000256", "12345678909", "jordi@zup.com.br", "jordi");
        this.contaDois = new Conta("0001", "000257", "71700673076", "rafael@zup.com.br", "rafael");

        BigDecimal saldo = new BigDecimal("50");
        contaUm.depositar(saldo);

        contaRepository.saveAll(List.of(contaUm, contaDois));
    }

    @Test
    @DisplayName("deve realizar a transferência entre contas")
    void test() throws Exception {
        BigDecimal valorTransferencia = new BigDecimal("25");

        TransferenciaRequest transferenciaRequest = new TransferenciaRequest(
                contaUm.getId(), contaDois.getId(), valorTransferencia
        );

        String payload = jsonUtils.toJson(transferenciaRequest);

        MockHttpServletRequestBuilder request = post("/transferencias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        String location = mockMvc.perform(request)
                .andExpect(
                        status().isCreated()
                )
                .andExpect(
                        redirectedUrlPattern("http://localhost/transferencias/*")
                )
                .andReturn()
                .getResponse()
                .getHeader("Location");

        assertNotNull(location);

        Long id = Long.valueOf(extrairId(location));

        assertTrue(
                transferenciaRepository.existsById(id),
                "deveria existir uma transferência para este id"
        );

        Conta contaUmAposTransferencia = contaRepository.findById(contaUm.getId()).get();
        Conta contaDoisAposTransferencia = contaRepository.findById(contaDois.getId()).get();

        assertEquals(
                contaUm.getSaldo().subtract(valorTransferencia).setScale(0),
                contaUmAposTransferencia.getSaldo().setScale(0),
                "O saldo da conta de origem deve corresponder a subtracao do valor da transferencia no saldo inicial"
        );
        assertEquals(
                contaDois.getSaldo().add(valorTransferencia).setScale(0),
                contaDoisAposTransferencia.getSaldo().setScale(0),
                "O saldo da conta de destino deve corresponder a soma do saldo inicial e o valor da transferencia"
        );


    }


    @Test
    @DisplayName("nao deve realizar uma transferencia com dados nulos")
    void test1() throws Exception {

        TransferenciaRequest transferenciaRequest = new TransferenciaRequest(
                null, null, null
        );

        String payloadRequest = jsonUtils.toJson(transferenciaRequest);

        MockHttpServletRequestBuilder request = post("/transferencias")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest);

        String payloadResponse = mockMvc.perform(request)
                .andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        List<String> response = jsonUtils.toListObject(String.class, payloadResponse);

        assertThat(response)
                .hasSize(3)
                .contains(
                        "O Campo valor não deve ser nulo",
                        "O Campo idDestino não deve ser nulo",
                        "O Campo idOrigem não deve ser nulo"
                );

    }

    @Test
    @DisplayName("nao deve realizar uma transferencia com dados invalidos")
    void test2() throws Exception {

        TransferenciaRequest transferenciaRequest = new TransferenciaRequest(
                -1L, -1L, BigDecimal.ZERO
        );

        String payloadRequest = jsonUtils.toJson(transferenciaRequest);

        MockHttpServletRequestBuilder request = post("/transferencias")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest);

        String payloadResponse = mockMvc.perform(request)
                .andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        List<String> response = jsonUtils.toListObject(String.class, payloadResponse);

        assertThat(response)
                .hasSize(3)
                .contains(
                        "O Campo idOrigem deve ser maior que 0",
                        "O Campo valor deve ser maior que 0",
                        "O Campo idDestino deve ser maior que 0"
                );

    }


    @Test
    @DisplayName("nao deve realizar a transferencia quando a conta de Origem não existe")
    void test3() throws Exception {
        TransferenciaRequest transferenciaRequest = new TransferenciaRequest(
                Long.MAX_VALUE, contaDois.getId(), BigDecimal.ONE
        );

        String payloadRequest = jsonUtils.toJson(transferenciaRequest);

        MockHttpServletRequestBuilder request = post("/transferencias")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isUnprocessableEntity()
                )
                .andReturn()
                .getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        ResponseStatusException exception = (ResponseStatusException) resolvedException;
        assertEquals("Não existe cadastro de conta para o id de origem", exception.getReason());
    }

    @Test
    @DisplayName("nao deve realizar a transferencia quando a conta de Destino não existe")
    void test4() throws Exception {
        TransferenciaRequest transferenciaRequest = new TransferenciaRequest(
                contaUm.getId(), Long.MAX_VALUE, BigDecimal.ONE
        );

        String payloadRequest = jsonUtils.toJson(transferenciaRequest);

        MockHttpServletRequestBuilder request = post("/transferencias")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isUnprocessableEntity()
                )
                .andReturn()
                .getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        ResponseStatusException exception = (ResponseStatusException) resolvedException;
        assertEquals("Não existe cadastro de conta para o id de destino", exception.getReason());
    }


    @Test
    @DisplayName("nao deve realizar a transferencia caso a conta de origem nao tenha saldo suficiente")
    void test5() throws Exception {
        TransferenciaRequest transferenciaRequest = new TransferenciaRequest(
                contaDois.getId(), contaUm.getId(), BigDecimal.ONE
        );

        String payloadRequest = jsonUtils.toJson(transferenciaRequest);

        MockHttpServletRequestBuilder request = post("/transferencias")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isUnprocessableEntity()
                )
                .andReturn()
                .getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(SaldoInsuficienteException.class, resolvedException.getClass());
        SaldoInsuficienteException exception = (SaldoInsuficienteException) resolvedException;
        assertEquals("não é possivel sacar um valor maior do que o saldo atual da conta.", exception.getMessage());
    }

    //testes para concorrencia
}