package ro.facultate.pos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ro.facultate.pos.dto.CreateClientRequest;
import ro.facultate.pos.entity.Client;
import ro.facultate.pos.service.ClientService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createClient_success_returns201() throws Exception {
        CreateClientRequest req = new CreateClientRequest();
        req.setNume("Maria Ionescu");
        req.setEmail("maria@example.com");
        req.setTelefon("0722000000");

        Client saved = new Client();
        saved.setId(1L);
        saved.setNume("Maria Ionescu");
        saved.setEmail("maria@example.com");
        saved.setTelefon("0722000000");

        Mockito.when(clientService.create(Mockito.any(CreateClientRequest.class)))
                .thenReturn(saved);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nume").value("Maria Ionescu"))
                .andExpect(jsonPath("$.email").value("maria@example.com"))
                .andExpect(jsonPath("$.telefon").value("0722000000"));
    }

    @Test
    void createClient_invalidEmail_returns400() throws Exception {
        //email invalid - @Email
        String body = """
                {
                  "nume": "Maria Ionescu",
                  "email": "maria.com",
                  "telefon": "0722000000"
                }
                """;

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createClient_blankName_returns400() throws Exception {
        //nume blank - @NotBlank
        String body = """
                {
                  "nume": "",
                  "email": "maria@example.com",
                  "telefon": "0722000000"
                }
                """;

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAll_success_returns200() throws Exception {
        Client c1 = new Client();
        c1.setId(1L);
        c1.setNume("Maria");

        Mockito.when(clientService.getAll()).thenReturn(List.of(c1));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nume").value("Maria"));
    }
}
