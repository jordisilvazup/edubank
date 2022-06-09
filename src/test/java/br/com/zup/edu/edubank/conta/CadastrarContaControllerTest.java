package br.com.zup.edu.edubank.conta;

import br.com.zup.edu.edubank.util.BaseIntegrationTest;
import br.com.zup.edu.edubank.util.JsonUtilsTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CadastrarContaControllerTest extends BaseIntegrationTest {
    @Autowired
    private JsonUtilsTest jsonUtils;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private TransferenciaRepository transferenciaRepository;

    @BeforeEach
    void setUp() {
        transferenciaRepository.deleteAll();
        contaRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        contaRepository.deleteAll();
    }

    @Test
    @DisplayName("deve cadastrar uma conta")
    void test() throws Exception {
        ContaRequest contaRequest = new ContaRequest(
                "jordi@zup.com.br",
                "349.556.070-04",
                "Jordi Henrique Marques da Silva",
                "000011",
                "0001"
        );

        String payload = jsonUtils.toJson(contaRequest);

        MockHttpServletRequestBuilder request = post("/contas")
                .contentType(APPLICATION_JSON)
                .content(payload);

        String location = mockMvc.perform(request)
                .andExpect(
                        status().isCreated()
                )
                .andExpect(
                        redirectedUrlPattern("http://localhost/contas/*")
                )
                .andReturn()
                .getResponse()
                .getHeader("Location");

        assertNotNull(location);

        Long id = Long.valueOf(extrairId(location));

        assertTrue(contaRepository.existsById(id), "deveria existir um id para esta conta");
    }

    @Test
    @DisplayName("nao deve cadastrar uma conta onde o email já exista no sistema")
    void unicidadeEmail() throws Exception {
        String emailJaCadastrado = "rafael.ponte@zup.com.br";

        Conta rafaelPonte = new Conta("0002", "000023", "682.085.520-62", emailJaCadastrado, "Rafael Ponte");

        contaRepository.save(rafaelPonte);

        ContaRequest contaRequest = new ContaRequest(
                emailJaCadastrado,
                "349.556.070-04",
                "Jordi Henrique Marques da Silva",
                "000011",
                "0001"
        );

        String payload = jsonUtils.toJson(contaRequest);

        MockHttpServletRequestBuilder request = post("/contas")
                .contentType(APPLICATION_JSON)
                .content(payload);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isUnprocessableEntity()
                )
                .andReturn()
                .getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());

        ResponseStatusException exception = (ResponseStatusException) resolvedException;

        assertEquals("Já existe cadastro de conta para este email.", exception.getReason());
    }

    @Test
    @DisplayName("nao deve cadastrar uma conta onde o cpf já exista no sistema")
    void unicidadeCpf() throws Exception {
        String cpfJaCadastrado = "682.085.520-62";

        Conta rafaelPonte = new Conta("0002", "000023", cpfJaCadastrado, "rafael.ponte@zup.com.br", "Rafael Ponte");
        contaRepository.save(rafaelPonte);

        ContaRequest contaRequest = new ContaRequest(
                "jordi@email.com",
                cpfJaCadastrado,
                "Jordi Henrique Marques da Silva",
                "000011",
                "0001"
        );

        String payload = jsonUtils.toJson(contaRequest);

        MockHttpServletRequestBuilder request = post("/contas")
                .contentType(APPLICATION_JSON)
                .content(payload);

        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isUnprocessableEntity()
                )
                .andReturn()
                .getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());

        ResponseStatusException exception = (ResponseStatusException) resolvedException;

        assertEquals("Já existe cadastro de conta para este cpf.", exception.getReason());
    }

    @Test
    @DisplayName("nao deve cadastrar uma conta com dados invalidos")
    void test1() throws Exception {
        ContaRequest contaRequest = new ContaRequest(
                null,
                "",
                null,
                null,
                ""
        );

        String payload = jsonUtils.toJson(contaRequest);

        MockHttpServletRequestBuilder request = post("/contas")
                .header("Accept-Language", "pt-br")
                .contentType(APPLICATION_JSON)
                .content(payload);

        String payloadResponse = mockMvc.perform(request)
                .andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(UTF_8);

        List<String> response = jsonUtils.toListObject(String.class, payloadResponse);

        assertThat(response)
                .hasSize(7)
                .contains(
                        "O Campo cpf não deve estar em branco",
                        "O Campo numeroDaConta não deve estar em branco",
                        "O Campo cpf número do registro de contribuinte individual brasileiro (CPF) inválido",
                        "O Campo nome não deve estar em branco",
                        "O Campo agencia não deve estar em branco",
                        "O Campo agencia deve conter 4 digitos",
                        "O Campo email não deve estar em branco"
                );
    }


    @Test
    @DisplayName("nao deve cadastrar uma conta com email em formato invalido")
    void test2() throws Exception {
        ContaRequest contaRequest = new ContaRequest(
                "jordizup.com.br",
                "349.556.070-04",
                "Jordi Henrique Marques da Silva",
                "000011",
                "0001"
        );


        String payload = jsonUtils.toJson(contaRequest);

        MockHttpServletRequestBuilder request = post("/contas")
                .header("Accept-Language", "pt-br")
                .contentType(APPLICATION_JSON)
                .content(payload);

        String payloadResponse = mockMvc.perform(request)
                .andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(UTF_8);

        List<String> response = jsonUtils.toListObject(String.class, payloadResponse);

        assertThat(response)
                .hasSize(1)
                .contains(
                        "O Campo email deve ser um endereço de e-mail bem formado"
                );
    }


    @Test
    @DisplayName("nao deve cadastrar uma conta com cpf invalido")
    void test3() throws Exception {
        ContaRequest contaRequest = new ContaRequest(
                "jordi@zup.com.br",
                "34955607003",
                "Jordi Henrique Marques da Silva",
                "000011",
                "0001"
        );


        String payload = jsonUtils.toJson(contaRequest);

        MockHttpServletRequestBuilder request = post("/contas")
                .header("Accept-Language", "pt-br")
                .contentType(APPLICATION_JSON)
                .content(payload);

        String payloadResponse = mockMvc.perform(request)
                .andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(UTF_8);

        List<String> response = jsonUtils.toListObject(String.class, payloadResponse);

        assertThat(response)
                .hasSize(1)
                .contains(
                        "O Campo cpf número do registro de contribuinte individual brasileiro (CPF) inválido"
                );
    }

    @Test
    @DisplayName("nao deve cadastrar uma conta com numero da conta contendo letras")
    void test4() throws Exception {
        ContaRequest contaRequest = new ContaRequest(
                "jordi@zup.com.br",
                "34955607004",
                "Jordi Henrique Marques da Silva",
                "0000R1",
                "0001"
        );


        String payload = jsonUtils.toJson(contaRequest);

        MockHttpServletRequestBuilder request = post("/contas")
                .header("Accept-Language", "pt-br")
                .contentType(APPLICATION_JSON)
                .content(payload);

        String payloadResponse = mockMvc.perform(request)
                .andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(UTF_8);

        List<String> response = jsonUtils.toListObject(String.class, payloadResponse);

        assertThat(response)
                .hasSize(1)
                .contains(
                        "O Campo numeroDaConta os digitos devem ser caracteres numericos"
                );
    }

    @Test
    @DisplayName("nao deve cadastrar uma conta onde o  numero contenha 7 ou mais caracteres")
    void test5() throws Exception {
        ContaRequest contaRequest = new ContaRequest(
                "jordi@zup.com.br",
                "34955607004",
                "Jordi Henrique Marques da Silva",
                "0000011",
                "0001"
        );


        String payload = jsonUtils.toJson(contaRequest);

        MockHttpServletRequestBuilder request = post("/contas")
                .header("Accept-Language", "pt-br")
                .contentType(APPLICATION_JSON)
                .content(payload);

        String payloadResponse = mockMvc.perform(request)
                .andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(UTF_8);

        List<String> response = jsonUtils.toListObject(String.class, payloadResponse);

        assertThat(response)
                .hasSize(1)
                .contains(
                        "O Campo numeroDaConta deve conter 6 digitos"
                );
    }

    @Test
    @DisplayName("nao deve cadastrar uma conta com agencia contendo letras")
    void test6() throws Exception {
        ContaRequest contaRequest = new ContaRequest(
                "jordi@zup.com.br",
                "34955607004",
                "Jordi Henrique Marques da Silva",
                "000011",
                "00E1"
        );


        String payload = jsonUtils.toJson(contaRequest);

        MockHttpServletRequestBuilder request = post("/contas")
                .header("Accept-Language", "pt-br")
                .contentType(APPLICATION_JSON)
                .content(payload);

        String payloadResponse = mockMvc.perform(request)
                .andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(UTF_8);

        List<String> response = jsonUtils.toListObject(String.class, payloadResponse);

        assertThat(response)
                .hasSize(1)
                .contains(
                        "O Campo agencia os digitos devem ser caracteres numericos"
                );
    }

    @Test
    @DisplayName("nao deve cadastrar uma conta onde a agencia contenha 5 ou mais caracteres")
    void test7() throws Exception {
        ContaRequest contaRequest = new ContaRequest(
                "jordi@zup.com.br",
                "34955607004",
                "Jordi Henrique Marques da Silva",
                "000001",
                "00015"
        );


        String payload = jsonUtils.toJson(contaRequest);

        MockHttpServletRequestBuilder request = post("/contas")
                .header("Accept-Language", "pt-br")
                .contentType(APPLICATION_JSON)
                .content(payload);

        String payloadResponse = mockMvc.perform(request)
                .andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(UTF_8);

        List<String> response = jsonUtils.toListObject(String.class, payloadResponse);

        assertThat(response)
                .hasSize(1)
                .contains(
                        "O Campo agencia deve conter 4 digitos"
                );
    }
}