package com.empresa.epi.controller;

import com.empresa.epi.dto.DevolucaoRequest;
import com.empresa.epi.dto.EntregaRequest;
import com.empresa.epi.model.Entrega;
import com.empresa.epi.model.ItemEntrega;
import com.empresa.epi.repository.EntregaRepository;
import com.empresa.epi.service.EntregaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/entregas")
public class EntregaController {
    private final EntregaService service;
    private final EntregaRepository repo;

    public EntregaController(EntregaService service, EntregaRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    @GetMapping public List<Entrega> listar() { return repo.findAll(Sort.by("id")); }

    @GetMapping("/{id}")
    public ResponseEntity<Entrega> buscar(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // O responsável é sempre o usuário logado
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Entrega registrar(@Valid @RequestBody EntregaRequest req, Authentication auth) {
        return service.registrar(req, auth.getName());
    }

    @PostMapping("/itens/{idItem}/devolucao")
    public ItemEntrega devolver(@PathVariable Long idItem, @Valid @RequestBody DevolucaoRequest req,
                                Authentication auth) {
        return service.devolver(idItem, req, auth.getName());
    }
}
