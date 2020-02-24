package com.rj.dinosaurs.catalogue.service;

import com.rj.dinosaurs.catalogue.domain.Epoch;
import com.rj.dinosaurs.catalogue.repository.EpochRepository;
import com.rj.dinosaurs.catalogue.repository.search.EpochSearchRepository;
import com.rj.dinosaurs.catalogue.service.dto.EpochDTO;
import com.rj.dinosaurs.catalogue.service.mapper.EpochMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Epoch}.
 */
@Service
@Transactional
public class EpochService {

    private final Logger log = LoggerFactory.getLogger(EpochService.class);

    private final EpochRepository epochRepository;

    private final EpochMapper epochMapper;

    private final EpochSearchRepository epochSearchRepository;

    public EpochService(EpochRepository epochRepository, EpochMapper epochMapper, EpochSearchRepository epochSearchRepository) {
        this.epochRepository = epochRepository;
        this.epochMapper = epochMapper;
        this.epochSearchRepository = epochSearchRepository;
    }

    /**
     * Save a epoch.
     *
     * @param epochDTO the entity to save.
     * @return the persisted entity.
     */
    public EpochDTO save(EpochDTO epochDTO) {
        log.debug("Request to save Epoch : {}", epochDTO);
        Epoch epoch = epochMapper.toEntity(epochDTO);
        epoch = epochRepository.save(epoch);
        EpochDTO result = epochMapper.toDto(epoch);
        epochSearchRepository.save(epoch);
        return result;
    }

    /**
     * Get all the epoches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EpochDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Epoches");
        return epochRepository.findAll(pageable)
            .map(epochMapper::toDto);
    }

    /**
     * Get one epoch by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EpochDTO> findOne(Long id) {
        log.debug("Request to get Epoch : {}", id);
        return epochRepository.findById(id)
            .map(epochMapper::toDto);
    }

    /**
     * Delete the epoch by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Epoch : {}", id);
        epochRepository.deleteById(id);
        epochSearchRepository.deleteById(id);
    }

    /**
     * Search for the epoch corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EpochDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Epoches for query {}", query);
        return epochSearchRepository.search(queryStringQuery(query), pageable)
            .map(epochMapper::toDto);
    }
}
