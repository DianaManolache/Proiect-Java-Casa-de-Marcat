package ro.facultate.pos.service;

import org.springframework.stereotype.Service;
import ro.facultate.pos.dto.CreateClientRequest;
import ro.facultate.pos.entity.Client;
import ro.facultate.pos.repository.ClientRepository;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client create(CreateClientRequest req) {
        Client c = new Client();
        c.setNume(req.getNume());
        c.setEmail(req.getEmail());
        c.setTelefon(req.getTelefon());
        return clientRepository.save(c);
    }

    public List<Client> getAll() {
        return clientRepository.findAll();
    }
}
