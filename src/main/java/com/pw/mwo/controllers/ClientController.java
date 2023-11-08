package com.pw.mwo.controllers;

import com.pw.mwo.domain.Client;
import com.pw.mwo.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<List<Client>> getClients() {
        return ResponseEntity.ok(clientService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable long id) {
        return ResponseEntity.ok(clientService.getClient(id));
    }

    @PostMapping
    public ResponseEntity<Void> createClient(@RequestBody Client client) {
        Client created = clientService.createClient(client);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(created.getId());
        return ResponseEntity.created(location).build();
    }

    @PatchMapping
    public ResponseEntity<Client> updateClient(@RequestBody Client client) {
        return ResponseEntity.ok(clientService.updateClient(client));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
