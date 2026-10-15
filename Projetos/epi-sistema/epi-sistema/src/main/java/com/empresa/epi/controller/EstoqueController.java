package com.empresa.epi.controller;

import com.empresa.epi.dto.AjusteRequest;
import com.empresa.epi.dto.EntradaRequest;
import com.empresa.epi.model.Epi;
import com.empresa.epi.model.Movimentacao;
import com.empresa.epi.repository.MovimentacaoRepository;
import com.empresa.epi.service.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EstoqueController {
    private final EstoqueService service;
    private final MovimentacaoRepository movRepo;

    public EstoqueController(EstoqueService service, MovimentacaoRepository movRepo) {
        this.service = service;
        this.movRepo = movRepo;
    }

    @PostMapping("/estoque/entrada")
    public Epi entrada(@Valid @RequestBody EntradaRequest req, Authentication auth) {
        return service.entrada(req, auth.getName());
    }

    @PostMapping("/estoque/ajuste")
    public Epi ajuste(@Valid @RequestBody AjusteRequest req, Authentication auth) {
        return service.ajuste(req, auth.getName());
    }

    @GetMapping("/movimentacoes")
    public List<Movimentacao> movimentacoes() { return movRepo.findAll(Sort.by("id")); }
}
