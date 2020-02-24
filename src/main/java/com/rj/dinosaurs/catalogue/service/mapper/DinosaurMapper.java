package com.rj.dinosaurs.catalogue.service.mapper;


import com.rj.dinosaurs.catalogue.domain.*;
import com.rj.dinosaurs.catalogue.service.dto.DinosaurDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dinosaur} and its DTO {@link DinosaurDTO}.
 */
@Mapper(componentModel = "spring", uses = {EpochMapper.class, CladeMapper.class})
public interface DinosaurMapper extends EntityMapper<DinosaurDTO, Dinosaur> {

    @Mapping(source = "epochItLived.id", target = "epochItLivedId")
    @Mapping(source = "clade.id", target = "cladeId")
    @Mapping(source = "clade.name", target = "cladeName")
    DinosaurDTO toDto(Dinosaur dinosaur);

    @Mapping(source = "epochItLivedId", target = "epochItLived")
    @Mapping(source = "cladeId", target = "clade")
    Dinosaur toEntity(DinosaurDTO dinosaurDTO);

    default Dinosaur fromId(Long id) {
        if (id == null) {
            return null;
        }
        Dinosaur dinosaur = new Dinosaur();
        dinosaur.setId(id);
        return dinosaur;
    }
}
