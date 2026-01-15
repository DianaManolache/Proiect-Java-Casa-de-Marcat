package ro.facultate.pos.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;
import ro.facultate.pos.dto.CreateProdusRequest;
import ro.facultate.pos.dto.UpdateStocRequest;
import ro.facultate.pos.entity.Categorie;
import ro.facultate.pos.entity.Produs;
import ro.facultate.pos.repository.CategorieRepository;
import ro.facultate.pos.repository.ProdusRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProdusServiceTest {

    private ProdusRepository produsRepository;
    private CategorieRepository categorieRepository;
    private ProdusService produsService;

    @BeforeEach
    void setUp() {
        produsRepository = Mockito.mock(ProdusRepository.class);
        categorieRepository = Mockito.mock(CategorieRepository.class);
        produsService = new ProdusService(produsRepository, categorieRepository);
    }

    @Test
    void create_shouldSaveProdus_withCategorie() {
        CreateProdusRequest req = new CreateProdusRequest();
        req.setNume("Paine");
        req.setPret(BigDecimal.valueOf(3.5));
        req.setStoc(10);
        req.setCategorieId(1L);

        Categorie cat = new Categorie();
        cat.setId(1L);
        cat.setNume("Panificatie");

        Mockito.when(categorieRepository.findById(1L)).thenReturn(Optional.of(cat));

        Produs saved = new Produs();
        saved.setId(5L);
        saved.setNume("Paine");
        saved.setPret(BigDecimal.valueOf(3.5));
        saved.setStoc(10);
        saved.setCategorie(cat);

        Mockito.when(produsRepository.save(Mockito.any(Produs.class))).thenReturn(saved);

        Produs result = produsService.create(req);

        assertEquals(5L, result.getId());
        assertEquals("Paine", result.getNume());

        ArgumentCaptor<Produs> captor = ArgumentCaptor.forClass(Produs.class);
        Mockito.verify(produsRepository).save(captor.capture());
        assertEquals(cat, captor.getValue().getCategorie());
    }

    @Test
    void create_shouldThrow404_whenCategorieNotFound() {
        CreateProdusRequest req = new CreateProdusRequest();
        req.setNume("Paine");
        req.setPret(BigDecimal.valueOf(3.5));
        req.setStoc(10);
        req.setCategorieId(999L);

        Mockito.when(categorieRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> produsService.create(req));

        Mockito.verify(produsRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void updateStoc_shouldUpdate_whenProdusExists() {
        Produs produs = new Produs();
        produs.setId(1L);
        produs.setNume("Paine");
        produs.setPret(BigDecimal.valueOf(3.5));
        produs.setStoc(10);

        Mockito.when(produsRepository.findById(1L)).thenReturn(Optional.of(produs));
        Mockito.when(produsRepository.save(Mockito.any(Produs.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateStocRequest req = new UpdateStocRequest();
        req.setStoc(100);

        Produs updated = produsService.updateStoc(1L, req);

        assertEquals(100, updated.getStoc());
        Mockito.verify(produsRepository).save(Mockito.any(Produs.class));
    }

    @Test
    void updateStoc_shouldThrow404_whenProdusNotFound() {
        Mockito.when(produsRepository.findById(999L)).thenReturn(Optional.empty());

        UpdateStocRequest req = new UpdateStocRequest();
        req.setStoc(100);

        assertThrows(ResponseStatusException.class, () -> produsService.updateStoc(999L, req));
        Mockito.verify(produsRepository, Mockito.never()).save(Mockito.any());
    }
}
