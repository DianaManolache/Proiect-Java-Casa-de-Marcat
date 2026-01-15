package ro.facultate.pos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ro.facultate.pos.dto.AddBonProdusRequest;
import ro.facultate.pos.dto.BonDetailsResponse;
import ro.facultate.pos.dto.CreateBonRequest;
import ro.facultate.pos.dto.PayBonRequest;
import ro.facultate.pos.entity.Bon;
import ro.facultate.pos.entity.BonProdus;
import ro.facultate.pos.entity.Plata;
import ro.facultate.pos.entity.enums.BonStatus;
import ro.facultate.pos.entity.enums.StatusPlata;
import ro.facultate.pos.entity.enums.TipPlata;
import ro.facultate.pos.service.BonService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BonController.class)
class BonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BonService bonService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createBon_success_returns201() throws Exception {
        CreateBonRequest req = new CreateBonRequest();
        req.setClientId(1L);
        req.setVanzatorId(1L);

        Bon saved = new Bon();
        saved.setId(10L);
        saved.setStatus(BonStatus.OPEN);

        Mockito.when(bonService.create(Mockito.any(CreateBonRequest.class))).thenReturn(saved);

        mockMvc.perform(post("/api/bons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void createBon_invalid_returns400() throws Exception {
        // clientId lipsa - @NotNull
        String body = """
                { "vanzatorId": 1 }
                """;

        mockMvc.perform(post("/api/bons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addProdus_success_returns201() throws Exception {
        AddBonProdusRequest req = new AddBonProdusRequest();
        req.setProdusId(1L);
        req.setCantitate(2);

        BonProdus bp = new BonProdus();
        bp.setId(55L);
        bp.setCantitate(2);

        Mockito.when(bonService.addProdus(Mockito.eq(10L), Mockito.any(AddBonProdusRequest.class)))
                .thenReturn(bp);

        mockMvc.perform(post("/api/bons/10/produse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(55))
                .andExpect(jsonPath("$.cantitate").value(2));
    }

    @Test
    void addProdus_invalid_returns400() throws Exception {
        // cantitate lipsa - @NotNull
        String body = """
                { "produsId": 1 }
                """;

        mockMvc.perform(post("/api/bons/10/produse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBonDetails_success_returns200() throws Exception {
        BonDetailsResponse resp = new BonDetailsResponse();
        resp.setId(10L);
        resp.setStatus(BonStatus.OPEN);
        resp.setData(LocalDateTime.now());
        resp.setClientId(1L);
        resp.setVanzatorId(1L);
        resp.setTotal(BigDecimal.valueOf(12.0));
        resp.setProduse(List.of());

        Mockito.when(bonService.getDetails(10L)).thenReturn(resp);

        mockMvc.perform(get("/api/bons/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.total").value(12.0));
    }

    @Test
    void payBon_success_returns201() throws Exception {
        PayBonRequest req = new PayBonRequest();
        req.setTipPlata(TipPlata.CASH);

        Plata plata = new Plata();
        plata.setId(99L);
        plata.setTip(TipPlata.CASH);
        plata.setSuma(BigDecimal.valueOf(12.0));
        plata.setStatus(StatusPlata.SUCCESS);

        Mockito.when(bonService.payBon(Mockito.eq(10L), Mockito.eq(TipPlata.CASH)))
                .thenReturn(plata);

        mockMvc.perform(post("/api/bons/10/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(99))
                .andExpect(jsonPath("$.tip").value("CASH"))
                .andExpect(jsonPath("$.suma").value(12.0))
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    void payBon_invalid_returns400() throws Exception {
        // tipPlata lipsa - @NotNull
        String body = "{}";

        mockMvc.perform(post("/api/bons/10/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPlati_success_returns200() throws Exception {
        Plata p1 = new Plata();
        p1.setId(1L);
        p1.setTip(TipPlata.CARD);

        Mockito.when(bonService.getPlati(10L)).thenReturn(List.of(p1));

        mockMvc.perform(get("/api/bons/10/plati"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].tip").value("CARD"));
    }
}
