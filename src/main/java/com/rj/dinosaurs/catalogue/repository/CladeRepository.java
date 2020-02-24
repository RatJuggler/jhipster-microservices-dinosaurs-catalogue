package com.rj.dinosaurs.catalogue.repository;

import com.rj.dinosaurs.catalogue.domain.Clade;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Clade entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CladeRepository extends JpaRepository<Clade, Long> {

}
