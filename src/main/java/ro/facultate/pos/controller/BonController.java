package ro.facultate.pos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.facultate.pos.dto.CreateBonRequest;
import ro.facultate.pos.entity.Bon;
import ro.facultate.pos.service.BonService;
import ro.facultate.pos.dto.AddBonProdusRequest;
import ro.facultate.pos.entity.BonProdus;
import ro.facultate.pos.dto.BonDetailsResponse;
import ro.facultate.pos.dto.PayBonRequest;
import ro.facultate.pos.entity.Plata;

import java.util.List;

@Tag(
        name = "Bonuri si Plati",
        description = "Operatii pentru gestionarea procesului de cumparare"
)
@RestController
@RequestMapping("/api/bons")
public class BonController {

    private final BonService bonService;

    public BonController(BonService bonService) {
        this.bonService = bonService;
    }

    @Operation(
            summary = "Creeaza bon",
            description = "Creeaza un bon nou cu status OPEN pentru un client si un vanzator"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Bon creat cu succes"),
            @ApiResponse(responseCode = "400", description = "Date invalide (validare)"),
            @ApiResponse(responseCode = "404", description = "Client sau vanzator inexistent")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Bon create(@Valid @RequestBody CreateBonRequest req) {
        return bonService.create(req);
    }

    @Operation(
            summary = "Adauga produs pe bon",
            description = "Adauga un produs pe un bon OPEN si scade stocul produsului"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produs adaugat pe bon"),
            @ApiResponse(responseCode = "400", description = "Bon inchis sau stoc insuficient"),
            @ApiResponse(responseCode = "404", description = "Bon sau produs inexistent")
    })
    @PostMapping("/{bonId}/produse")
    @ResponseStatus(HttpStatus.CREATED)
    public BonProdus addProdus(
            @PathVariable Long bonId,
            @Valid @RequestBody AddBonProdusRequest req) {
        return bonService.addProdus(bonId, req);
    }

    @Operation(
            summary = "Detalii bon",
            description = "Returneaza detaliile unui bon impreuna cu produsele si totalul"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalii bon returnate"),
            @ApiResponse(responseCode = "404", description = "Bon inexistent")
    })
    @GetMapping("/{bonId}")
    public BonDetailsResponse getDetails(@PathVariable Long bonId) {
        return bonService.getDetails(bonId);
    }

    @Operation(
            summary = "Plateste bon",
            description = "Proceseaza plata unui bon OPEN si il marcheaza ca PAID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Plata efectuata cu succes"),
            @ApiResponse(responseCode = "400", description = "Bon deja inchis"),
            @ApiResponse(responseCode = "404", description = "Bon inexistent")
    })
    @PostMapping("/{bonId}/pay")
    @ResponseStatus(HttpStatus.CREATED)
    public Plata payBon(
            @PathVariable Long bonId,
            @Valid @RequestBody PayBonRequest req) {
        return bonService.payBon(bonId, req.getTipPlata());
    }

    @Operation(
            summary = "Listeaza platile unui bon",
            description = "Returneaza toate platile asociate unui bon"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista plati returnata"),
            @ApiResponse(responseCode = "404", description = "Bon inexistent")
    })
    @GetMapping("/{bonId}/plati")
    public List<Plata> getPlati(@PathVariable Long bonId) {
        return bonService.getPlati(bonId);
    }
}
