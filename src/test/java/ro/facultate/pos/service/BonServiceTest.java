package ro.facultate.pos.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ro.facultate.pos.dto.AddBonProdusRequest;
import ro.facultate.pos.entity.*;
import ro.facultate.pos.entity.enums.BonStatus;
import ro.facultate.pos.entity.enums.StatusPlata;
import ro.facultate.pos.entity.enums.TipPlata;
import ro.facultate.pos.repository.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BonServiceTest {

    private BonRepository bonRepository;
    private ClientRepository clientRepository;
    private VanzatorRepository vanzatorRepository;
    private BonProdusRepository bonProdusRepository;
    private ProdusRepository produsRepository;
    private PlataRepository plataRepository;

    private BonService bonService;

    @BeforeEach
    void setUp() {
        bonRepository = Mockito.mock(BonRepository.class);
        clientRepository = Mockito.mock(ClientRepository.class);
        vanzatorRepository = Mockito.mock(VanzatorRepository.class);
        bonProdusRepository = Mockito.mock(BonProdusRepository.class);
        produsRepository = Mockito.mock(ProdusRepository.class);
        plataRepository = Mockito.mock(PlataRepository.class);

        bonService = new BonService(
                bonRepository,
                clientRepository,
                vanzatorRepository,
                bonProdusRepository,
                produsRepository,
                plataRepository
        );
    }

    @Test
    void addProdus_shouldDecreaseStock_andSaveBonProdus() {
        // bon OPEN
        Bon bon = new Bon();
        bon.setId(1L);
        bon.setStatus(BonStatus.OPEN);

        Mockito.when(bonRepository.findById(1L)).thenReturn(Optional.of(bon));

        // produs cu stoc 10
        Produs produs = new Produs();
        produs.setId(2L);
        produs.setNume("Paine");
        produs.setPret(BigDecimal.valueOf(3.5));
        produs.setStoc(10);

        Mockito.when(produsRepository.findById(2L)).thenReturn(Optional.of(produs));
        Mockito.when(produsRepository.save(Mockito.any(Produs.class))).thenAnswer(inv -> inv.getArgument(0));
        Mockito.when(bonProdusRepository.save(Mockito.any(BonProdus.class))).thenAnswer(inv -> inv.getArgument(0));

        AddBonProdusRequest req = new AddBonProdusRequest();
        req.setProdusId(2L);
        req.setCantitate(3);

        BonProdus saved = bonService.addProdus(1L, req);

        // stoc scazut
        assertEquals(7, produs.getStoc());

        // linia salvata cu pretUnitar = pret produs
        assertEquals(3, saved.getCantitate());
        assertEquals(BigDecimal.valueOf(3.5), saved.getPretUnitar());
        assertEquals(1L, saved.getBon().getId());
        assertEquals(2L, saved.getProdus().getId());

        // verificam ca s a facut save la produs si la bonProdus
        Mockito.verify(produsRepository).save(Mockito.any(Produs.class));
        Mockito.verify(bonProdusRepository).save(Mockito.any(BonProdus.class));
    }

    @Test
    void addProdus_shouldThrow_whenStockInsufficient() {
        Bon bon = new Bon();
        bon.setId(1L);
        bon.setStatus(BonStatus.OPEN);

        Mockito.when(bonRepository.findById(1L)).thenReturn(Optional.of(bon));

        Produs produs = new Produs();
        produs.setId(2L);
        produs.setPret(BigDecimal.valueOf(3.5));
        produs.setStoc(2); // insuficient

        Mockito.when(produsRepository.findById(2L)).thenReturn(Optional.of(produs));

        AddBonProdusRequest req = new AddBonProdusRequest();
        req.setProdusId(2L);
        req.setCantitate(5);

        assertThrows(RuntimeException.class, () -> bonService.addProdus(1L, req));

        // n ar trebui sa salveze nimic
        Mockito.verify(bonProdusRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(produsRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void payBon_shouldCreatePayment_andMarkBonPaid() {
        Bon bon = new Bon();
        bon.setId(1L);
        bon.setStatus(BonStatus.OPEN);

        Mockito.when(bonRepository.findById(1L)).thenReturn(Optional.of(bon));

        // bon are 2 linii: 2 * 3.5 = 7.0, 1 * 5.0 = 5.0, total 12.0
        Produs p1 = new Produs();
        p1.setId(10L);
        p1.setNume("Paine");
        p1.setPret(BigDecimal.valueOf(3.5));

        BonProdus l1 = new BonProdus();
        l1.setBon(bon);
        l1.setProdus(p1);
        l1.setCantitate(2);
        l1.setPretUnitar(BigDecimal.valueOf(3.5));

        Produs p2 = new Produs();
        p2.setId(11L);
        p2.setNume("Suc");
        p2.setPret(BigDecimal.valueOf(5.0));

        BonProdus l2 = new BonProdus();
        l2.setBon(bon);
        l2.setProdus(p2);
        l2.setCantitate(1);
        l2.setPretUnitar(BigDecimal.valueOf(5.0));

        Mockito.when(bonProdusRepository.findByBonId(1L)).thenReturn(List.of(l1, l2));
        Mockito.when(plataRepository.save(Mockito.any(Plata.class))).thenAnswer(inv -> inv.getArgument(0));
        Mockito.when(bonRepository.save(Mockito.any(Bon.class))).thenAnswer(inv -> inv.getArgument(0));

        Plata plata = bonService.payBon(1L, TipPlata.CASH);

        assertEquals(TipPlata.CASH, plata.getTip());
        assertEquals(BigDecimal.valueOf(12.0), plata.getSuma());
        assertEquals(StatusPlata.SUCCESS, plata.getStatus());
        assertNotNull(plata.getData());

        assertEquals(BonStatus.PAID, bon.getStatus());

        // retinem ce s-a salvat in plataRepository
        ArgumentCaptor<Plata> captor = ArgumentCaptor.forClass(Plata.class);
        Mockito.verify(plataRepository).save(captor.capture());
        assertEquals(BigDecimal.valueOf(12.0), captor.getValue().getSuma());

        Mockito.verify(bonRepository).save(Mockito.any(Bon.class));
    }

    @Test
    void payBon_shouldThrowResponseStatusException_whenBusinessExceptionOccurs() {
        Mockito.when(bonRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(org.springframework.web.server.ResponseStatusException.class,
                () -> bonService.payBon(999L, TipPlata.CASH));
    }
}
