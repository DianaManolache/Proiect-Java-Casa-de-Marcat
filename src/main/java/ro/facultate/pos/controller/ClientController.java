package ro.facultate.pos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.facultate.pos.dto.CreateClientRequest;
import ro.facultate.pos.entity.Client;
import ro.facultate.pos.service.ClientService;

import java.util.List;

@Tag(
        name = "Clienti",
        description = "Operatii pentru gestionarea clientilor"
)
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(
            summary = "Creeaza client",
            description = "Creeaza un client nou"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client creat cu succes"),
            @ApiResponse(responseCode = "400", description = "Date invalide (validare)")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client create(@Valid @RequestBody CreateClientRequest req) {
        return clientService.create(req);
    }

    @Operation(
            summary = "Listeaza clienti",
            description = "Returneaza lista tuturor clientilor"
    )
    @ApiResponse(responseCode = "200", description = "Lista clienti returnata")
    @GetMapping
    public List<Client> getAll() {
        return clientService.getAll();
    }
}
