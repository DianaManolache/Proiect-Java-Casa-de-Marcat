package ro.facultate.pos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ro.facultate.pos.dto.CreateCategorieRequest;
import ro.facultate.pos.entity.Categorie;
import ro.facultate.pos.service.CategorieService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategorieController.class)
class CategorieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategorieService categorieService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCategorie_success_returns201() throws Exception {
        CreateCategorieRequest req = new CreateCategorieRequest();
        req.setNume("Panificatie");

        Categorie saved = new Categorie();
        saved.setId(1L);
        saved.setNume("Panificatie");

        Mockito.when(categorieService.create(Mockito.any(CreateCategorieRequest.class)))
                .thenReturn(saved);

        mockMvc.perform(post("/api/categorii")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nume").value("Panificatie"));
    }

    @Test
    void createCategorie_invalid_returns400() throws Exception {
        //nume blank - @NotBlank
        String body = "{\"nume\":\"\"}";

        mockMvc.perform(post("/api/categorii")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAll_success_returns200() throws Exception {
        Categorie c1 = new Categorie();
        c1.setId(1L);
        c1.setNume("Panificatie");

        Mockito.when(categorieService.getAll()).thenReturn(List.of(c1));

        mockMvc.perform(get("/api/categorii"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nume").value("Panificatie"));
    }
}
