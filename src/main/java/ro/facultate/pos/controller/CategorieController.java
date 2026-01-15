package ro.facultate.pos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.facultate.pos.dto.CreateCategorieRequest;
import ro.facultate.pos.entity.Categorie;
import ro.facultate.pos.service.CategorieService;

import java.util.List;

@Tag(
        name = "Categorii",
        description = "Operatii pentru gestionarea categoriilor"
)
@RestController
@RequestMapping("/api/categorii")
public class CategorieController {

    private final CategorieService categorieService;

    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    @Operation(
            summary = "Creeaza categorie",
            description = "Creeaza o categorie noua de produse"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categorie creata cu succes"),
            @ApiResponse(responseCode = "400", description = "Date invalide (validare)")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Categorie create(@Valid @RequestBody CreateCategorieRequest req) {
        return categorieService.create(req);
    }

    @Operation(
            summary = "Listeaza categorii",
            description = "Returneaza lista tuturor categoriilor existente"
    )
    @ApiResponse(responseCode = "200", description = "Lista categorii returnata")
    @GetMapping
    public List<Categorie> getAll() {
        return categorieService.getAll();
    }
}
