package br.com.zup.edu.edubank.conta;

import br.com.zup.edu.edubank.util.BaseIntegrationTest;
import br.com.zup.edu.edubank.util.JsonUtilsTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ConsultarSaldoControllerTest extends BaseIntegrationTest {
    @Autowired
    private JsonUtilsTest jsonUtils;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private TransferenciaRepository transferenciaRepository;

    private Conta conta;

    @BeforeEach
    void setUp() {
        transferenciaRepository.deleteAll();
        contaRepository.deleteAll();
        this.conta = new Conta("0001", "000256", "12345678909", "jordi@zup.com.br", "jordi");
        contaRepository.save(conta);
    }

    @Test
    @DisplayName("deve consultar o saldo de uma conta")
    void test() throws Exception {
        MockHttpServletRequestBuilder request = get("/contas/{id}", conta.getId())
                .contentType(APPLICATION_JSON);

        String payload = mockMvc.perform(request)
                .andExpect(
                        status().isOk()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(UTF_8);

        ConsultarSaldoResponse response = jsonUtils.toObject(ConsultarSaldoResponse.class, payload);

        assertThat(response)
                .isNotNull()
                .extracting(
                        ConsultarSaldoResponse::getAgencia,
                        ConsultarSaldoResponse::getConta,
                        ConsultarSaldoResponse::getSaldo
                )
                .contains(
                        conta.getAgencia(),
                        conta.getNumeroDaConta(),
                        conta.getSaldo().setScale(2)
                );

    }

    @Test
    @DisplayName("nao deve consultar o saldo de uma conta inexistente")
    void test1() throws Exception {
        MockHttpServletRequestBuilder request = get("/contas/{id}", Long.MAX_VALUE)
                .contentType(APPLICATION_JSON);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isNotFound()
                )
                .andReturn()
                .getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class,resolvedException.getClass());
        assertEquals("Conta não cadastrada no sistema.",((ResponseStatusException) resolvedException).getReason());
    }
}