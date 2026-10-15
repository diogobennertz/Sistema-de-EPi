package com.empresa.epi.service;

import com.empresa.epi.dto.EmprestimoRequest;
import com.empresa.epi.exception.RegraNegocioException;
import com.empresa.epi.model.*;
import com.empresa.epi.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepo;
    private final ItemEmprestimoRepository itemRepo;
    private final ColaboradorRepository colaboradorRepo;
    private final EpiRepository epiRepo;
    private final UsuarioRepository usuarioRepo;

    public EmprestimoService(EmprestimoRepository emprestimoRepo, ItemEmprestimoRepository itemRepo,
                             ColaboradorRepository colaboradorRepo, EpiRepository epiRepo,
                             UsuarioRepository usuarioRepo) {
        this.emprestimoRepo = emprestimoRepo;
        this.itemRepo = itemRepo;
        this.colaboradorRepo = colaboradorRepo;
        this.epiRepo = epiRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Transactional
    public Emprestimo registrar(EmprestimoRequest req, String loginUsuario) {
        Colaborador colaborador = colaboradorRepo.findById(req.idColaborador())
                .orElseThrow(() -> new RegraNegocioException("Colaborador não encontrado."));
        if (!colaborador.isAtivo())
            throw new RegraNegocioException("Colaborador inativo.");

        Usuario usuario = usuarioRepo.findByLogin(loginUsuario)
                .orElseThrow(() -> new RegraNegocioException("Usuário logado não encontrado."));

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setColaborador(colaborador);
        emprestimo.setUsuario(usuario);
        emprestimo.setDataHoraEntrega(LocalDateTime.now());
        emprestimo.setObservacao(req.observacao());

        for (EmprestimoRequest.ItemRequest ir : req.itens()) {
            Epi epi = epiRepo.findById(ir.idEpi())
                    .orElseThrow(() -> new RegraNegocioException("EPI não encontrado: " + ir.idEpi()));
            if (!epi.isAtivo())
                throw new RegraNegocioException("EPI inativo: " + epi.getNome());
            if (epi.getValidadeCa() != null && epi.getValidadeCa().isBefore(LocalDate.now()))
                throw new RegraNegocioException("CA vencido para o EPI: " + epi.getNome());
            if (epi.getQuantidadeEstoque() < ir.quantidade())
                throw new RegraNegocioException("Estoque insuficiente para " + epi.getNome()
                        + " (disponível: " + epi.getQuantidadeEstoque() + ").");

            epi.setQuantidadeEstoque(epi.getQuantidadeEstoque() - ir.quantidade());

            ItemEmprestimo item = new ItemEmprestimo();
            item.setEmprestimo(emprestimo);
            item.setEpi(epi);
            item.setQuantidade(ir.quantidade());
            item.setDataPrevistaDevolucao(ir.dataPrevistaDevolucao());
            emprestimo.getItens().add(item);
        }
        return emprestimoRepo.save(emprestimo);
    }

    @Transactional
    public ItemEmprestimo devolver(Long idItem) {
        ItemEmprestimo item = itemRepo.findById(idItem)
                .orElseThrow(() -> new RegraNegocioException("Item não encontrado."));
        if (item.getStatus() == StatusItem.DEVOLVIDO)
            throw new RegraNegocioException("Item já devolvido.");

        item.setStatus(StatusItem.DEVOLVIDO);
        item.setDataDevolucao(LocalDate.now());
        Epi epi = item.getEpi();
        epi.setQuantidadeEstoque(epi.getQuantidadeEstoque() + item.getQuantidade());
        return item;
    }
}
