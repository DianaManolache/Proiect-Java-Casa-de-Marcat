package ro.facultate.pos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.facultate.pos.dto.CreateVanzatorRequest;
import ro.facultate.pos.entity.Vanzator;
import ro.facultate.pos.service.VanzatorService;

import java.util.List;

@Tag(
        name = "Vanzatori",
        description = "Operatii pentru gestionarea vanzatorilor"
)
@RestController
@RequestMapping("/api/vanzatori")
public class VanzatorController {

    private final VanzatorService vanzatorService;

    public VanzatorController(VanzatorService vanzatorService) {
        this.vanzatorService = vanzatorService;
    }

    @Operation(
            summary = "Adauga vanzator",
            description = "Adauga un vanzator nou"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vanzator adaugat cu succes"),
            @ApiResponse(responseCode = "400", description = "Date invalide (validare)"),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vanzator create(@Valid @RequestBody CreateVanzatorRequest req) {
        return vanzatorService.create(req);
    }

    @Operation(
            summary = "Listeaza vanzatorii",
            description = "Returneaza lista cu toti vanzatorii"
    )
    @ApiResponse(responseCode = "200", description = "Lista vanzatori")
    @GetMapping
    public List<Vanzator> getAll() {
        return vanzatorService.getAll();
    }
}
