package com.empresa.epi.controller;

import com.empresa.epi.model.Colaborador;
import com.empresa.epi.repository.ColaboradorRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/colaboradores")
public class ColaboradorController {
    private final ColaboradorRepository repo;
    public ColaboradorController(ColaboradorRepository repo) { this.repo = repo; }

    @GetMapping public List<Colaborador> listar() { return repo.findAll(Sort.by("id")); }

    @PostMapping
    public Colaborador criar(@Valid @RequestBody Colaborador c) { c.setId(null); return repo.save(c); }

    @PutMapping("/{id}")
    public ResponseEntity<Colaborador> atualizar(@PathVariable Long id, @Valid @RequestBody Colaborador c) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        c.setId(id);
        return ResponseEntity.ok(repo.save(c));
    }

    // Exclusão lógica: preserva o histórico
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        return repo.findById(id).map(c -> { c.setAtivo(false); repo.save(c); return ResponseEntity.noContent().<Void>build(); })
                .orElse(ResponseEntity.notFound().build());
    }
}
