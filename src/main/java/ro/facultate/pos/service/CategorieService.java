package ro.facultate.pos.service;

import org.springframework.stereotype.Service;
import ro.facultate.pos.dto.CreateCategorieRequest;
import ro.facultate.pos.entity.Categorie;
import ro.facultate.pos.repository.CategorieRepository;

import java.util.List;

@Service
public class CategorieService {

    private final CategorieRepository categorieRepository;

    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    public Categorie create(CreateCategorieRequest req) {
        Categorie c = new Categorie();
        c.setNume(req.getNume());
        return categorieRepository.save(c);
    }

    public List<Categorie> getAll() {
        return categorieRepository.findAll();
    }
}
