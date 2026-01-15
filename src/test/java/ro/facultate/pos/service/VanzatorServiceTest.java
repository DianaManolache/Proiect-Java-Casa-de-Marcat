package ro.facultate.pos.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ro.facultate.pos.dto.CreateVanzatorRequest;
import ro.facultate.pos.entity.Vanzator;
import ro.facultate.pos.repository.VanzatorRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VanzatorServiceTest {

    private final VanzatorRepository vanzatorRepository = Mockito.mock(VanzatorRepository.class);
    private final VanzatorService vanzatorService = new VanzatorService(vanzatorRepository);

    @Test
    void create_shouldSaveVanzator() {
        CreateVanzatorRequest req = new CreateVanzatorRequest();
        req.setNume("Vanzator 1");

        Vanzator saved = new Vanzator();
        saved.setId(1L);
        saved.setNume("Vanzator 1");

        Mockito.when(vanzatorRepository.save(Mockito.any(Vanzator.class))).thenReturn(saved);

        Vanzator result = vanzatorService.create(req);

        assertEquals(1L, result.getId());
        assertEquals("Vanzator 1", result.getNume());

        ArgumentCaptor<Vanzator> captor = ArgumentCaptor.forClass(Vanzator.class);
        Mockito.verify(vanzatorRepository).save(captor.capture());
        assertEquals("Vanzator 1", captor.getValue().getNume());
    }

    @Test
    void getAll_shouldReturnList() {
        Mockito.when(vanzatorRepository.findAll()).thenReturn(List.of(new Vanzator()));

        List<Vanzator> result = vanzatorService.getAll();

        assertEquals(1, result.size());
        Mockito.verify(vanzatorRepository).findAll();
    }
}
