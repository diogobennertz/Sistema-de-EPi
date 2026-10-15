package com.empresa.epi.repository;

import com.empresa.epi.model.Epi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpiRepository extends JpaRepository<Epi, Long> {
}
