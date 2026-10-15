package com.empresa.epi.controller;

import com.empresa.epi.model.CategoriaEpi;
import com.empresa.epi.repository.CategoriaEpiRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    private final CategoriaEpiRepository repo;
    public CategoriaController(CategoriaEpiRepository repo) { this.repo = repo; }

    @GetMapping public List<CategoriaEpi> listar() { return repo.findAll(); }

    @PostMapping
    public CategoriaEpi criar(@Valid @RequestBody CategoriaEpi c) { c.setId(null); return repo.save(c); }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaEpi> atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaEpi c) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        c.setId(id);
        return ResponseEntity.ok(repo.save(c));
    }

    // Retorna 409 se houver EPIs usando a categoria (tratado no GlobalExceptionHandler)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
