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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ListarTransferenciasControllerTest extends BaseIntegrationTest {
    @Autowired
    private JsonUtilsTest jsonUtils;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private TransferenciaRepository transferenciaRepository;
    private Conta contaUm;
    private Conta contaDois;
    private Conta contaTres;
    private Conta contaQuatro;
    private Transferencia contaUmParaDois15;
    private Transferencia contaUmParaDois10;
    private Transferencia contaTresParaUm10;
    private Transferencia contaTresParaDois5;
    private Transferencia contaDoisParaTres11;


    @BeforeEach
    void setUp() {
        transferenciaRepository.deleteAll();
        contaRepository.deleteAll();

        this.contaUm = new Conta("0001", "000256", "12345678909", "jordi@zup.com.br", "jordi");
        this.contaDois = new Conta("0001", "000257", "71700673076", "rafael@zup.com.br", "rafael");
        this.contaTres = new Conta("0001", "000258", "60672020009", "yuri@zup.com.br", "yuri");
        this.contaQuatro = new Conta("0001", "000259", "39011911008", "paula@zup.com.br", "paula");
        contaRepository.saveAll(
                List.of(contaUm, contaDois, contaTres, contaQuatro)
        );

        this.contaUmParaDois15 = new Transferencia(contaUm, contaDois, new BigDecimal("15").setScale(1));
        this.contaUmParaDois10 = new Transferencia(contaUm, contaDois, new BigDecimal("10").setScale(1));
        this.contaTresParaUm10 = new Transferencia(contaTres, contaUm, new BigDecimal("10").setScale(1));
        this.contaTresParaDois5 = new Transferencia(contaTres, contaDois, new BigDecimal("5").setScale(1));
        this.contaDoisParaTres11 = new Transferencia(contaTres, contaDois, new BigDecimal("11").setScale(1));
        transferenciaRepository.saveAll(
                List.of(contaUmParaDois15, contaUmParaDois10, contaTresParaUm10, contaTresParaDois5, contaDoisParaTres11)
        );
    }


    @Test
    @DisplayName("deve listar as transferencias de uma conta")
    void test() throws Exception {
        MockHttpServletRequestBuilder request = get("/contas/{id}/transferencias", contaUm.getId())
                .contentType(APPLICATION_JSON);


        String payload = mockMvc.perform(request)
                .andExpect(
                        status().isOk()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(UTF_8);

        List<TransferenciaResponse> transferenciasResponse = jsonUtils.toListObjectResultPage(
                TransferenciaResponse.class, payload
        );
        System.out.println(transferenciasResponse);

        assertThat(transferenciasResponse)
                .isNotNull()
                .hasSize(3)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("realizadoEm")
                .contains(
                        new TransferenciaResponse(contaUmParaDois15),
                        new TransferenciaResponse(contaUmParaDois10),
                        new TransferenciaResponse(contaTresParaUm10)
                );

        assertThat(transferenciasResponse.get(0).getRealizadoEm())
                .isEqualToIgnoringNanos(contaUmParaDois15.getCriadoEm());
        assertThat(transferenciasResponse.get(1).getRealizadoEm())
                .isEqualToIgnoringNanos(contaUmParaDois10.getCriadoEm());
        assertThat(transferenciasResponse.get(2).getRealizadoEm())
                .isEqualToIgnoringNanos(contaTresParaUm10.getCriadoEm());

    }

    @Test
    @DisplayName("deve listar um conjunto vazio de transferencias de uma conta")
    void test1() throws Exception {
        MockHttpServletRequestBuilder request = get("/contas/{id}/transferencias", contaQuatro.getId())
                .contentType(APPLICATION_JSON);


        String payload = mockMvc.perform(request)
                .andExpect(
                        status().isOk()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(UTF_8);

        List<TransferenciaResponse> transferenciasResponse = jsonUtils.toListObjectResultPage(
                TransferenciaResponse.class, payload
        );
        System.out.println(transferenciasResponse);

        assertThat(transferenciasResponse)
                .isNotNull()
                .hasSize(0);
    }

    @Test
    @DisplayName("nao deve listar as transferencias de uma conta inexistente")
    void test2() throws Exception {
        MockHttpServletRequestBuilder request = get("/contas/{id}/transferencias", Long.MAX_VALUE)
                .contentType(APPLICATION_JSON);


        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isNotFound()
                )
                .andReturn()
                .getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        assertEquals("Conta não cadastrada no sistema.", ((ResponseStatusException )resolvedException).getReason());


    }
}