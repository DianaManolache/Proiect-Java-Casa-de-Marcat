package ro.facultate.pos.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ro.facultate.pos.dto.CreateClientRequest;
import ro.facultate.pos.entity.Client;
import ro.facultate.pos.repository.ClientRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientServiceTest {

    private final ClientRepository clientRepository = Mockito.mock(ClientRepository.class);
    private final ClientService clientService = new ClientService(clientRepository);

    @Test
    void create_shouldSaveClient() {
        CreateClientRequest req = new CreateClientRequest();
        req.setNume("Maria");
        req.setEmail("maria@example.com");
        req.setTelefon("0722000000");

        Client saved = new Client();
        saved.setId(1L);
        saved.setNume("Maria");
        saved.setEmail("maria@example.com");
        saved.setTelefon("0722000000");

        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(saved);

        Client result = clientService.create(req);

        assertEquals(1L, result.getId());
        assertEquals("Maria", result.getNume());

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        Mockito.verify(clientRepository).save(captor.capture());
        assertEquals("Maria", captor.getValue().getNume());
        assertEquals("maria@example.com", captor.getValue().getEmail());
        assertEquals("0722000000", captor.getValue().getTelefon());
    }

    @Test
    void getAll_shouldReturnList() {
        Mockito.when(clientRepository.findAll()).thenReturn(List.of(new Client(), new Client()));

        List<Client> result = clientService.getAll();

        assertEquals(2, result.size());
        Mockito.verify(clientRepository).findAll();
    }
}
