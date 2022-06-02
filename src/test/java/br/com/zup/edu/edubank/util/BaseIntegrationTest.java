package br.com.zup.edu.edubank.util;

import br.com.zup.edu.edubank.EdubankApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = EdubankApplication.class)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("Test")
public class BaseIntegrationTest {
    protected String extrairId(String location) {
        int proximoCaracterAposUltimaBarra = location.lastIndexOf("/") + 1;

        return location.substring(proximoCaracterAposUltimaBarra);
    }
}
