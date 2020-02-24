package com.rj.dinosaurs.catalogue.web.rest;

import com.rj.dinosaurs.catalogue.service.EpochService;
import com.rj.dinosaurs.catalogue.web.rest.errors.BadRequestAlertException;
import com.rj.dinosaurs.catalogue.service.dto.EpochDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.rj.dinosaurs.catalogue.domain.Epoch}.
 */
@RestController
@RequestMapping("/api")
public class EpochResource {

    private final Logger log = LoggerFactory.getLogger(EpochResource.class);

    private static final String ENTITY_NAME = "catalogueEpoch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EpochService epochService;

    public EpochResource(EpochService epochService) {
        this.epochService = epochService;
    }

    /**
     * {@code POST  /epoches} : Create a new epoch.
     *
     * @param epochDTO the epochDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new epochDTO, or with status {@code 400 (Bad Request)} if the epoch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/epoches")
    public ResponseEntity<EpochDTO> createEpoch(@Valid @RequestBody EpochDTO epochDTO) throws URISyntaxException {
        log.debug("REST request to save Epoch : {}", epochDTO);
        if (epochDTO.getId() != null) {
            throw new BadRequestAlertException("A new epoch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EpochDTO result = epochService.save(epochDTO);
        return ResponseEntity.created(new URI("/api/epoches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /epoches} : Updates an existing epoch.
     *
     * @param epochDTO the epochDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated epochDTO,
     * or with status {@code 400 (Bad Request)} if the epochDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the epochDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/epoches")
    public ResponseEntity<EpochDTO> updateEpoch(@Valid @RequestBody EpochDTO epochDTO) throws URISyntaxException {
        log.debug("REST request to update Epoch : {}", epochDTO);
        if (epochDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EpochDTO result = epochService.save(epochDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, epochDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /epoches} : get all the epoches.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of epoches in body.
     */
    @GetMapping("/epoches")
    public ResponseEntity<List<EpochDTO>> getAllEpoches(Pageable pageable) {
        log.debug("REST request to get a page of Epoches");
        Page<EpochDTO> page = epochService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /epoches/:id} : get the "id" epoch.
     *
     * @param id the id of the epochDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the epochDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/epoches/{id}")
    public ResponseEntity<EpochDTO> getEpoch(@PathVariable Long id) {
        log.debug("REST request to get Epoch : {}", id);
        Optional<EpochDTO> epochDTO = epochService.findOne(id);
        return ResponseUtil.wrapOrNotFound(epochDTO);
    }

    /**
     * {@code DELETE  /epoches/:id} : delete the "id" epoch.
     *
     * @param id the id of the epochDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/epoches/{id}")
    public ResponseEntity<Void> deleteEpoch(@PathVariable Long id) {
        log.debug("REST request to delete Epoch : {}", id);
        epochService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/epoches?query=:query} : search for the epoch corresponding
     * to the query.
     *
     * @param query the query of the epoch search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/epoches")
    public ResponseEntity<List<EpochDTO>> searchEpoches(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Epoches for query {}", query);
        Page<EpochDTO> page = epochService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
