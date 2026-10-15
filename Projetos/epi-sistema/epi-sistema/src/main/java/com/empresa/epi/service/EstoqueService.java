package com.empresa.epi.service;

import com.empresa.epi.dto.AjusteRequest;
import com.empresa.epi.dto.EntradaRequest;
import com.empresa.epi.exception.RegraNegocioException;
import com.empresa.epi.model.*;
import com.empresa.epi.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstoqueService {

    private final EpiRepository epiRepo;
    private final UsuarioRepository usuarioRepo;
    private final MovimentacaoRepository movRepo;

    public EstoqueService(EpiRepository epiRepo, UsuarioRepository usuarioRepo, MovimentacaoRepository movRepo) {
        this.epiRepo = epiRepo;
        this.usuarioRepo = usuarioRepo;
        this.movRepo = movRepo;
    }

    @Transactional
    public Epi entrada(EntradaRequest req, String emailUsuario) {
        Epi epi = epiAtivo(req.idEpi());
        Usuario usuario = usuario(emailUsuario);
        epi.setQuantidadeEstoque(epi.getQuantidadeEstoque() + req.quantidade());
        movRepo.save(Movimentacao.de(TipoMovimentacao.ENTRADA, epi, req.quantidade(), null, usuario,
                "—", req.observacao(), null));
        return epi;
    }

    @Transactional
    public Epi ajuste(AjusteRequest req, String emailUsuario) {
        Epi epi = epiAtivo(req.idEpi());
        Usuario usuario = usuario(emailUsuario);
        int diferenca = req.novoSaldo() - epi.getQuantidadeEstoque();
        epi.setQuantidadeEstoque(req.novoSaldo());
        movRepo.save(Movimentacao.de(TipoMovimentacao.AJUSTE, epi, diferenca, null, usuario,
                "—", req.justificativa(), null));
        return epi;
    }

    private Epi epiAtivo(Long id) {
        Epi epi = epiRepo.findById(id).orElseThrow(() -> new RegraNegocioException("EPI não encontrado."));
        if (!epi.isAtivo()) throw new RegraNegocioException("EPI inativo.");
        return epi;
    }

    private Usuario usuario(String email) {
        return usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RegraNegocioException("Usuário logado não encontrado."));
    }
}
