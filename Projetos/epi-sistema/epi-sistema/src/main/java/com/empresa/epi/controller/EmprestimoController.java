package com.empresa.epi.controller;

import com.empresa.epi.dto.EmprestimoRequest;
import com.empresa.epi.model.*;
import com.empresa.epi.repository.*;
import com.empresa.epi.service.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EmprestimoController {
    private final EmprestimoService service;
    private final EmprestimoRepository emprestimoRepo;
    private final ItemEmprestimoRepository itemRepo;

    public EmprestimoController(EmprestimoService service, EmprestimoRepository emprestimoRepo,
                                ItemEmprestimoRepository itemRepo) {
        this.service = service;
        this.emprestimoRepo = emprestimoRepo;
        this.itemRepo = itemRepo;
    }

    // Nova entrega (usuário responsável vem do login; data/hora gerada pelo servidor)
    @PostMapping("/emprestimos")
    @ResponseStatus(HttpStatus.CREATED)
    public Emprestimo registrar(@Valid @RequestBody EmprestimoRequest req, Authentication auth) {
        return service.registrar(req, auth.getName());
    }

    @GetMapping("/emprestimos")
    public List<Emprestimo> listar() { return emprestimoRepo.findAll(); }

    @GetMapping("/emprestimos/{id}")
    public ResponseEntity<Emprestimo> buscar(@PathVariable Long id) {
        return emprestimoRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Tela de devoluções: itens ainda não devolvidos
    @GetMapping("/itens/pendentes")
    public List<ItemEmprestimo> pendentes() { return itemRepo.findByStatus(StatusItem.PENDENTE); }

    // Tela "EPIs em uso" do colaborador
    @GetMapping("/colaboradores/{id}/epis-em-uso")
    public List<ItemEmprestimo> emUso(@PathVariable Long id) {
        return itemRepo.findByEmprestimoColaboradorIdAndStatus(id, StatusItem.PENDENTE);
    }

    @PutMapping("/itens/{id}/devolucao")
    public ItemEmprestimo devolver(@PathVariable Long id) { return service.devolver(id); }
}
