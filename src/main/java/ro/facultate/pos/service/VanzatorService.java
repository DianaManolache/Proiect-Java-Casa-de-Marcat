package ro.facultate.pos.service;

import org.springframework.stereotype.Service;
import ro.facultate.pos.dto.CreateVanzatorRequest;
import ro.facultate.pos.entity.Vanzator;
import ro.facultate.pos.repository.VanzatorRepository;

import java.util.List;

@Service
public class VanzatorService {

    private final VanzatorRepository vanzatorRepository;

    public VanzatorService(VanzatorRepository vanzatorRepository) {
        this.vanzatorRepository = vanzatorRepository;
    }

    public Vanzator create(CreateVanzatorRequest req) {
        Vanzator v = new Vanzator();
        v.setNume(req.getNume());
        return vanzatorRepository.save(v);
    }

    public List<Vanzator> getAll() {
        return vanzatorRepository.findAll();
    }
}
