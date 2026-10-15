package com.empresa.epi.controller;

import com.empresa.epi.model.Epi;
import com.empresa.epi.repository.EpiRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/epis")
public class EpiController {
    private final EpiRepository repo;
    public EpiController(EpiRepository repo) { this.repo = repo; }

    @GetMapping public List<Epi> listar() { return repo.findAll(Sort.by("id")); }

    // Novo EPI sempre nasce com estoque 0; o saldo entra por /api/estoque/entrada
    @PostMapping
    public Epi criar(@Valid @RequestBody Epi e) {
        e.setId(null);
        e.setQuantidadeEstoque(0);
        return repo.save(e);
    }

    // Editar o cadastro nunca altera o saldo de estoque
    @PutMapping("/{id}")
    public ResponseEntity<Epi> atualizar(@PathVariable Long id, @Valid @RequestBody Epi e) {
        return repo.findById(id).map(atual -> {
            e.setId(id);
            e.setQuantidadeEstoque(atual.getQuantidadeEstoque());
            return ResponseEntity.ok(repo.save(e));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        return repo.findById(id).map(e -> { e.setAtivo(false); repo.save(e); return ResponseEntity.noContent().<Void>build(); })
                .orElse(ResponseEntity.notFound().build());
    }
}
