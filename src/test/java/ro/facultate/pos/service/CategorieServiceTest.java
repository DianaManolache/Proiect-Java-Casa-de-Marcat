package ro.facultate.pos.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ro.facultate.pos.dto.CreateCategorieRequest;
import ro.facultate.pos.entity.Categorie;
import ro.facultate.pos.repository.CategorieRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategorieServiceTest {

    private final CategorieRepository categorieRepository = Mockito.mock(CategorieRepository.class);
    private final CategorieService categorieService = new CategorieService(categorieRepository);

    @Test
    void create_shouldSaveCategorie() {
        CreateCategorieRequest req = new CreateCategorieRequest();
        req.setNume("Panificatie");

        Categorie saved = new Categorie();
        saved.setId(1L);
        saved.setNume("Panificatie");

        Mockito.when(categorieRepository.save(Mockito.any(Categorie.class))).thenReturn(saved);

        Categorie result = categorieService.create(req);

        assertEquals(1L, result.getId());
        assertEquals("Panificatie", result.getNume());

        ArgumentCaptor<Categorie> captor = ArgumentCaptor.forClass(Categorie.class);
        Mockito.verify(categorieRepository).save(captor.capture());
        assertEquals("Panificatie", captor.getValue().getNume());
    }

    @Test
    void getAll_shouldReturnList() {
        Mockito.when(categorieRepository.findAll()).thenReturn(List.of(new Categorie()));

        List<Categorie> result = categorieService.getAll();

        assertEquals(1, result.size());
        Mockito.verify(categorieRepository).findAll();
    }
}
