package com.rj.dinosaurs.catalogue.repository;

import com.rj.dinosaurs.catalogue.domain.Dinosaur;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Dinosaur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DinosaurRepository extends JpaRepository<Dinosaur, Long> {

}
