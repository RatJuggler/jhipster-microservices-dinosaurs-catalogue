package com.rj.dinosaurs.catalogue.web.rest;

import com.rj.dinosaurs.catalogue.service.DinosaurService;
import com.rj.dinosaurs.catalogue.web.rest.errors.BadRequestAlertException;
import com.rj.dinosaurs.catalogue.service.dto.DinosaurDTO;

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
 * REST controller for managing {@link com.rj.dinosaurs.catalogue.domain.Dinosaur}.
 */
@RestController
@RequestMapping("/api")
public class DinosaurResource {

    private final Logger log = LoggerFactory.getLogger(DinosaurResource.class);

    private static final String ENTITY_NAME = "catalogueDinosaur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DinosaurService dinosaurService;

    public DinosaurResource(DinosaurService dinosaurService) {
        this.dinosaurService = dinosaurService;
    }

    /**
     * {@code POST  /dinosaurs} : Create a new dinosaur.
     *
     * @param dinosaurDTO the dinosaurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dinosaurDTO, or with status {@code 400 (Bad Request)} if the dinosaur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dinosaurs")
    public ResponseEntity<DinosaurDTO> createDinosaur(@Valid @RequestBody DinosaurDTO dinosaurDTO) throws URISyntaxException {
        log.debug("REST request to save Dinosaur : {}", dinosaurDTO);
        if (dinosaurDTO.getId() != null) {
            throw new BadRequestAlertException("A new dinosaur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DinosaurDTO result = dinosaurService.save(dinosaurDTO);
        return ResponseEntity.created(new URI("/api/dinosaurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dinosaurs} : Updates an existing dinosaur.
     *
     * @param dinosaurDTO the dinosaurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dinosaurDTO,
     * or with status {@code 400 (Bad Request)} if the dinosaurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dinosaurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dinosaurs")
    public ResponseEntity<DinosaurDTO> updateDinosaur(@Valid @RequestBody DinosaurDTO dinosaurDTO) throws URISyntaxException {
        log.debug("REST request to update Dinosaur : {}", dinosaurDTO);
        if (dinosaurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DinosaurDTO result = dinosaurService.save(dinosaurDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dinosaurDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /dinosaurs} : get all the dinosaurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dinosaurs in body.
     */
    @GetMapping("/dinosaurs")
    public ResponseEntity<List<DinosaurDTO>> getAllDinosaurs(Pageable pageable) {
        log.debug("REST request to get a page of Dinosaurs");
        Page<DinosaurDTO> page = dinosaurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dinosaurs/:id} : get the "id" dinosaur.
     *
     * @param id the id of the dinosaurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dinosaurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dinosaurs/{id}")
    public ResponseEntity<DinosaurDTO> getDinosaur(@PathVariable Long id) {
        log.debug("REST request to get Dinosaur : {}", id);
        Optional<DinosaurDTO> dinosaurDTO = dinosaurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dinosaurDTO);
    }

    /**
     * {@code DELETE  /dinosaurs/:id} : delete the "id" dinosaur.
     *
     * @param id the id of the dinosaurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dinosaurs/{id}")
    public ResponseEntity<Void> deleteDinosaur(@PathVariable Long id) {
        log.debug("REST request to delete Dinosaur : {}", id);
        dinosaurService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/dinosaurs?query=:query} : search for the dinosaur corresponding
     * to the query.
     *
     * @param query the query of the dinosaur search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/dinosaurs")
    public ResponseEntity<List<DinosaurDTO>> searchDinosaurs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Dinosaurs for query {}", query);
        Page<DinosaurDTO> page = dinosaurService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
