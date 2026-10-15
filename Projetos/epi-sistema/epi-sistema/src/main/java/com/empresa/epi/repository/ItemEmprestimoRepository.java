package com.empresa.epi.repository;

import com.empresa.epi.model.ItemEmprestimo;
import com.empresa.epi.model.StatusItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemEmprestimoRepository extends JpaRepository<ItemEmprestimo, Long> {
    List<ItemEmprestimo> findByStatus(StatusItem status);
    List<ItemEmprestimo> findByEmprestimoColaboradorIdAndStatus(Long idColaborador, StatusItem status);
}
