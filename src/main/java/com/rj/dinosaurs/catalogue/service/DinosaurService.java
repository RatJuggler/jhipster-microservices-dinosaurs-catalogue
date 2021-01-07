package com.rj.dinosaurs.catalogue.service;

import com.rj.dinosaurs.catalogue.domain.Dinosaur;
import com.rj.dinosaurs.catalogue.repository.DinosaurRepository;
import com.rj.dinosaurs.catalogue.repository.search.DinosaurSearchRepository;
import com.rj.dinosaurs.catalogue.service.dto.DinosaurDTO;
import com.rj.dinosaurs.catalogue.service.mapper.DinosaurMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Dinosaur}.
 */
@Service
@Transactional
public class DinosaurService {

    private final Logger log = LoggerFactory.getLogger(DinosaurService.class);

    private final DinosaurRepository dinosaurRepository;

    private final DinosaurMapper dinosaurMapper;

    private final DinosaurSearchRepository dinosaurSearchRepository;

    public DinosaurService(DinosaurRepository dinosaurRepository, DinosaurMapper dinosaurMapper, DinosaurSearchRepository dinosaurSearchRepository) {
        this.dinosaurRepository = dinosaurRepository;
        this.dinosaurMapper = dinosaurMapper;
        this.dinosaurSearchRepository = dinosaurSearchRepository;
    }

    /**
     * Save a dinosaur.
     *
     * @param dinosaurDTO the entity to save.
     * @return the persisted entity.
     */
    public DinosaurDTO save(DinosaurDTO dinosaurDTO) {
        log.debug("Request to save Dinosaur : {}", dinosaurDTO);
        Dinosaur dinosaur = dinosaurMapper.toEntity(dinosaurDTO);
        dinosaur = dinosaurRepository.save(dinosaur);
        DinosaurDTO result = dinosaurMapper.toDto(dinosaur);
        dinosaurSearchRepository.save(dinosaur);
        return result;
    }

    /**
     * Get all the dinosaurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DinosaurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Dinosaurs");
        return dinosaurRepository.findAll(pageable)
            .map(dinosaurMapper::toDto);
    }


    /**
     * Get one dinosaur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DinosaurDTO> findOne(Long id) {
        log.debug("Request to get Dinosaur : {}", id);
        return dinosaurRepository.findById(id)
            .map(dinosaurMapper::toDto);
    }

    /**
     * Delete the dinosaur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Dinosaur : {}", id);
        dinosaurRepository.deleteById(id);
        dinosaurSearchRepository.deleteById(id);
    }

    /**
     * Search for the dinosaur corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DinosaurDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Dinosaurs for query {}", query);
        return dinosaurSearchRepository.search(queryStringQuery(query), pageable)
            .map(dinosaurMapper::toDto);
    }
}
