package ro.facultate.pos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.facultate.pos.dto.CreateProdusRequest;
import ro.facultate.pos.entity.Produs;
import ro.facultate.pos.service.ProdusService;
import ro.facultate.pos.dto.UpdateStocRequest;

import java.util.List;

@Tag(
        name = "Produse",
        description = "Operatii pentru gestionarea produselor"
)
@RestController
@RequestMapping("/api/produse")
public class ProdusController {

    private final ProdusService produsService;

    public ProdusController(ProdusService produsService) {
        this.produsService = produsService;
    }

    @Operation(
            summary = "Creeaza produs",
            description = "Creeaza un produs nou si il asociaza unei categorii existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produs creat cu succes"),
            @ApiResponse(responseCode = "400", description = "Date invalide (validare)"),
            @ApiResponse(responseCode = "404", description = "Categoria nu exista")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produs create(@Valid @RequestBody CreateProdusRequest req) {
        return produsService.create(req);
    }

    @Operation(
            summary = "Listeaza produse",
            description = "Returneaza lista tuturor produselor"
    )
    @ApiResponse(responseCode = "200", description = "Lista produse")
    @GetMapping
    public List<Produs> getAll() {
        return produsService.getAll();
    }

    @Operation(
            summary = "Listeaza produse dupa categorie",
            description = "Returneaza lista tuturor produselor dintr-o categorie specificata"
    )
    @ApiResponse(responseCode = "200", description = "Lista produse")
    @GetMapping("/categorie/{categorieId}")
    public List<Produs> getByCategorie(@PathVariable Long categorieId) {
        return produsService.getByCategorie(categorieId);
    }

    @Operation(
            summary = "Modificare stoc produs",
            description = "Modifica stocul unui produs specificat"
    )
    @PutMapping("/{produsId}/stoc")
    public Produs updateStoc(
            @PathVariable Long produsId,
            @Valid @RequestBody UpdateStocRequest req) {
        return produsService.updateStoc(produsId, req);
    }
}
