package com.rj.dinosaurs.catalogue.repository;

import com.rj.dinosaurs.catalogue.domain.Epoch;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Epoch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EpochRepository extends JpaRepository<Epoch, Long> {
}
