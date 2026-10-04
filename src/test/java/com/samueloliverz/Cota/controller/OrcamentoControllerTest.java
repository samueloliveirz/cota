package com.samueloliverz.Cota.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class OrcamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String ORCAMENTO_VALIDO = """
            {
              "tipo": "FORMALIZADO",
              "empresa": "Metalúrgica Teste Ltda",
              "cnpj": "12.345.678/0001-90",
              "tipoRetirada": "LOJA",
              "formaPagamento": "A_VISTA",
              "frete": 45.50,
              "itens": [
                { "produto": "Cilindro hidráulico 50mm", "quantidade": 2, "precoUnitario": 850.00 },
                { "produto": "Kit de vedação", "quantidade": 4, "precoUnitario": 35.90 }
              ]
            }
            """;

    @Test
    void semLoginDeveMandarProLogin() throws Exception {
        mockMvc.perform(get("/orcamentos"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deveCriarOrcamentoECalcularOTotal() throws Exception {
        mockMvc.perform(post("/orcamentos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ORCAMENTO_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.setor").value("HIDRAULICA"))
                .andExpect(jsonPath("$.status").value("ENVIADO"))
                .andExpect(jsonPath("$.subtotal").value(1843.6))
                .andExpect(jsonPath("$.total").value(1889.1));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deveRecusarOrcamentoSemEmpresa() throws Exception {
        String semEmpresa = ORCAMENTO_VALIDO.replace("\"Metalúrgica Teste Ltda\"", "\"\"");

        mockMvc.perform(post("/orcamentos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(semEmpresa))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.empresa").value("Informe o nome da empresa"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deveRecusarPostSemTokenCsrf() throws Exception {
        mockMvc.perform(post("/orcamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ORCAMENTO_VALIDO))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "joao", roles = "USER")
    void vendedorNaoPodeVerUsuarios() throws Exception {
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isForbidden());
    }
}