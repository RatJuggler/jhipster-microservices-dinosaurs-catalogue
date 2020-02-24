package com.rj.dinosaurs.catalogue.service;

import com.rj.dinosaurs.catalogue.domain.Clade;
import com.rj.dinosaurs.catalogue.repository.CladeRepository;
import com.rj.dinosaurs.catalogue.repository.search.CladeSearchRepository;
import com.rj.dinosaurs.catalogue.service.dto.CladeDTO;
import com.rj.dinosaurs.catalogue.service.mapper.CladeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Clade}.
 */
@Service
@Transactional
public class CladeService {

    private final Logger log = LoggerFactory.getLogger(CladeService.class);

    private final CladeRepository cladeRepository;

    private final CladeMapper cladeMapper;

    private final CladeSearchRepository cladeSearchRepository;

    public CladeService(CladeRepository cladeRepository, CladeMapper cladeMapper, CladeSearchRepository cladeSearchRepository) {
        this.cladeRepository = cladeRepository;
        this.cladeMapper = cladeMapper;
        this.cladeSearchRepository = cladeSearchRepository;
    }

    /**
     * Save a clade.
     *
     * @param cladeDTO the entity to save.
     * @return the persisted entity.
     */
    public CladeDTO save(CladeDTO cladeDTO) {
        log.debug("Request to save Clade : {}", cladeDTO);
        Clade clade = cladeMapper.toEntity(cladeDTO);
        clade = cladeRepository.save(clade);
        CladeDTO result = cladeMapper.toDto(clade);
        cladeSearchRepository.save(clade);
        return result;
    }

    /**
     * Get all the clades.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CladeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Clades");
        return cladeRepository.findAll(pageable)
            .map(cladeMapper::toDto);
    }

    /**
     * Get one clade by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CladeDTO> findOne(Long id) {
        log.debug("Request to get Clade : {}", id);
        return cladeRepository.findById(id)
            .map(cladeMapper::toDto);
    }

    /**
     * Delete the clade by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Clade : {}", id);
        cladeRepository.deleteById(id);
        cladeSearchRepository.deleteById(id);
    }

    /**
     * Search for the clade corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CladeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Clades for query {}", query);
        return cladeSearchRepository.search(queryStringQuery(query), pageable)
            .map(cladeMapper::toDto);
    }
}
