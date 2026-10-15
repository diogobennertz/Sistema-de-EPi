package com.empresa.epi.service;

import com.empresa.epi.dto.DevolucaoRequest;
import com.empresa.epi.dto.EntregaRequest;
import com.empresa.epi.exception.RegraNegocioException;
import com.empresa.epi.model.*;
import com.empresa.epi.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Service
public class EntregaService {

    private static final Set<String> CONDICOES = Set.of("Bom", "Danificado", "Descartado");

    private final EntregaRepository entregaRepo;
    private final ItemEntregaRepository itemRepo;
    private final ColaboradorRepository colaboradorRepo;
    private final EpiRepository epiRepo;
    private final UsuarioRepository usuarioRepo;
    private final MovimentacaoRepository movRepo;

    public EntregaService(EntregaRepository entregaRepo, ItemEntregaRepository itemRepo,
                          ColaboradorRepository colaboradorRepo, EpiRepository epiRepo,
                          UsuarioRepository usuarioRepo, MovimentacaoRepository movRepo) {
        this.entregaRepo = entregaRepo;
        this.itemRepo = itemRepo;
        this.colaboradorRepo = colaboradorRepo;
        this.epiRepo = epiRepo;
        this.usuarioRepo = usuarioRepo;
        this.movRepo = movRepo;
    }

    @Transactional
    public Entrega registrar(EntregaRequest req, String emailUsuario) {
        Colaborador colaborador = colaboradorRepo.findById(req.idColaborador())
                .orElseThrow(() -> new RegraNegocioException("Colaborador não encontrado."));
        if (!colaborador.isAtivo())
            throw new RegraNegocioException("Não é possível entregar EPI a colaborador inativo.");

        Usuario usuario = usuarioRepo.findByEmail(emailUsuario)
                .orElseThrow(() -> new RegraNegocioException("Usuário logado não encontrado."));

        LocalDate data = req.dataEntrega() == null ? LocalDate.now() : req.dataEntrega();
        if (req.dataPrevistaDevolucao() != null && req.dataPrevistaDevolucao().isBefore(data))
            throw new RegraNegocioException("A previsão de devolução não pode ser anterior à data da entrega.");

        // Junta itens repetidos do mesmo EPI
        Map<Long, Integer> totais = new LinkedHashMap<>();
        for (EntregaRequest.ItemReq ir : req.itens())
            totais.merge(ir.idEpi(), ir.quantidade(), Integer::sum);

        Entrega entrega = new Entrega();
        entrega.setColaborador(colaborador);
        entrega.setUsuario(usuario);
        entrega.setDataEntrega(data);
        entrega.setDataPrevistaDevolucao(req.dataPrevistaDevolucao());
        entrega.setObservacao(req.observacao());

        for (Map.Entry<Long, Integer> t : totais.entrySet()) {
            Epi epi = epiRepo.findById(t.getKey())
                    .orElseThrow(() -> new RegraNegocioException("EPI não encontrado: " + t.getKey()));
            if (!epi.isAtivo())
                throw new RegraNegocioException("EPI inativo: " + epi.getNome());
            if (epi.getValidadeCa() != null && epi.getValidadeCa().isBefore(LocalDate.now()))
                throw new RegraNegocioException("CA vencido para o EPI: " + epi.getNome());
            if (epi.getQuantidadeEstoque() < t.getValue())
                throw new RegraNegocioException("Estoque insuficiente de " + epi.getNome()
                        + " (disponível: " + epi.getQuantidadeEstoque() + ").");

            epi.setQuantidadeEstoque(epi.getQuantidadeEstoque() - t.getValue());

            ItemEntrega item = new ItemEntrega();
            item.setEntrega(entrega);
            item.setEpi(epi);
            item.setQuantidade(t.getValue());
            entrega.getItens().add(item);
        }

        Entrega salva = entregaRepo.save(entrega);
        for (ItemEntrega item : salva.getItens())
            movRepo.save(Movimentacao.de(TipoMovimentacao.ENTREGA, item.getEpi(), item.getQuantidade(),
                    colaborador, usuario, "E-" + salva.getId(), null, null));
        return salva;
    }

    @Transactional
    public ItemEntrega devolver(Long idItem, DevolucaoRequest req, String emailUsuario) {
        ItemEntrega item = itemRepo.findById(idItem)
                .orElseThrow(() -> new RegraNegocioException("Item da entrega não encontrado."));
        Usuario usuario = usuarioRepo.findByEmail(emailUsuario)
                .orElseThrow(() -> new RegraNegocioException("Usuário logado não encontrado."));
        Epi epi = item.getEpi();

        if (!epi.isReutilizavel())
            throw new RegraNegocioException("EPI consumível não gera devolução.");
        if (!CONDICOES.contains(req.condicao()))
            throw new RegraNegocioException("Condição inválida (use Bom, Danificado ou Descartado).");

        int pendente = item.getQuantidade() - item.getQuantidadeDevolvida();
        if (req.quantidade() < 1 || req.quantidade() > pendente)
            throw new RegraNegocioException("Quantidade deve estar entre 1 e " + pendente + " (saldo pendente).");

        item.setQuantidadeDevolvida(item.getQuantidadeDevolvida() + req.quantidade());
        item.setCondicao(req.condicao());

        // Só volta ao estoque o que está em bom estado
        if ("Bom".equals(req.condicao()))
            epi.setQuantidadeEstoque(epi.getQuantidadeEstoque() + req.quantidade());

        String obs = req.condicao() + (req.observacao() == null || req.observacao().isBlank()
                ? "" : " · " + req.observacao());
        LocalDateTime quando = req.data() == null ? null : req.data().atTime(LocalTime.now());
        movRepo.save(Movimentacao.de(TipoMovimentacao.DEVOLUCAO, epi, req.quantidade(),
                item.getEntrega().getColaborador(), usuario, "E-" + item.getEntrega().getId(), obs, quando));
        return item;
    }
}
