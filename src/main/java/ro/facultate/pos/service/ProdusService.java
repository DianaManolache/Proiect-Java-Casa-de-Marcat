package ro.facultate.pos.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ro.facultate.pos.dto.CreateProdusRequest;
import ro.facultate.pos.entity.Categorie;
import ro.facultate.pos.entity.Produs;
import ro.facultate.pos.repository.CategorieRepository;
import ro.facultate.pos.repository.ProdusRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ro.facultate.pos.dto.UpdateStocRequest;

import java.util.List;

@Service
public class ProdusService {

    private final ProdusRepository produsRepository;
    private final CategorieRepository categorieRepository;

    public ProdusService(ProdusRepository produsRepository, CategorieRepository categorieRepository) {
        this.produsRepository = produsRepository;
        this.categorieRepository = categorieRepository;
    }

    public Produs create(CreateProdusRequest req) {
        Categorie categorie = categorieRepository.findById(req.getCategorieId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categorie not found"));

        Produs p = new Produs();
        p.setNume(req.getNume());
        p.setPret(req.getPret());
        p.setStoc(req.getStoc());
        p.setCategorie(categorie);

        return produsRepository.save(p);
    }

    public List<Produs> getAll() {
        return produsRepository.findAll();
    }

    public List<Produs> getByCategorie(Long categorieId) {
        return produsRepository.findByCategorieId(categorieId);
    }

    public Produs updateStoc(Long produsId, UpdateStocRequest req) {
        Produs produs = produsRepository.findById(produsId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produs not found"));

        produs.setStoc(req.getStoc());
        return produsRepository.save(produs);
    }
}
