package com.empresa.epi.controller;

import com.empresa.epi.model.Epi;
import com.empresa.epi.repository.EpiRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/epis")
public class EpiController {
    private final EpiRepository repo;
    public EpiController(EpiRepository repo) { this.repo = repo; }

    @GetMapping public List<Epi> listar() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Epi> buscar(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Epi criar(@Valid @RequestBody Epi e) { e.setId(null); return repo.save(e); }

    @PutMapping("/{id}")
    public ResponseEntity<Epi> atualizar(@PathVariable Long id, @Valid @RequestBody Epi e) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        e.setId(id);
        return ResponseEntity.ok(repo.save(e));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        return repo.findById(id).map(e -> { e.setAtivo(false); repo.save(e); return ResponseEntity.noContent().<Void>build(); })
                .orElse(ResponseEntity.notFound().build());
    }
}
