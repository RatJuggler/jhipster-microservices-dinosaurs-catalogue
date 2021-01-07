package com.rj.dinosaurs.catalogue.web.rest;

import com.rj.dinosaurs.catalogue.CatalogueApp;
import com.rj.dinosaurs.catalogue.domain.Dinosaur;
import com.rj.dinosaurs.catalogue.domain.Epoch;
import com.rj.dinosaurs.catalogue.domain.Clade;
import com.rj.dinosaurs.catalogue.repository.DinosaurRepository;
import com.rj.dinosaurs.catalogue.repository.search.DinosaurSearchRepository;
import com.rj.dinosaurs.catalogue.service.DinosaurService;
import com.rj.dinosaurs.catalogue.service.dto.DinosaurDTO;
import com.rj.dinosaurs.catalogue.service.mapper.DinosaurMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rj.dinosaurs.catalogue.domain.enumeration.Diet;
/**
 * Integration tests for the {@link DinosaurResource} REST controller.
 */
@SpringBootTest(classes = CatalogueApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class DinosaurResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRONUNCIATION = "AAAAAAAAAA";
    private static final String UPDATED_PRONUNCIATION = "BBBBBBBBBB";

    private static final String DEFAULT_MEANING = "AAAAAAAAAA";
    private static final String UPDATED_MEANING = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEIGHT = 0;
    private static final Integer UPDATED_WEIGHT = 1;

    private static final Integer DEFAULT_LENGTH = 0;
    private static final Integer UPDATED_LENGTH = 1;

    private static final Integer DEFAULT_HEIGHT = 0;
    private static final Integer UPDATED_HEIGHT = 1;

    private static final Diet DEFAULT_DIET = Diet.HERBIVORE;
    private static final Diet UPDATED_DIET = Diet.CARNIVORE;

    private static final Long DEFAULT_CREATED_BY = 0L;
    private static final Long UPDATED_CREATED_BY = 1L;

    private static final Instant DEFAULT_CREATED_DT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_MODIFIED_BY = 0L;
    private static final Long UPDATED_MODIFIED_BY = 1L;

    private static final Instant DEFAULT_MODIFIED_DT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DinosaurRepository dinosaurRepository;

    @Autowired
    private DinosaurMapper dinosaurMapper;

    @Autowired
    private DinosaurService dinosaurService;

    /**
     * This repository is mocked in the com.rj.dinosaurs.catalogue.repository.search test package.
     *
     * @see com.rj.dinosaurs.catalogue.repository.search.DinosaurSearchRepositoryMockConfiguration
     */
    @Autowired
    private DinosaurSearchRepository mockDinosaurSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDinosaurMockMvc;

    private Dinosaur dinosaur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dinosaur createEntity(EntityManager em) {
        Dinosaur dinosaur = new Dinosaur()
            .name(DEFAULT_NAME)
            .pronunciation(DEFAULT_PRONUNCIATION)
            .meaning(DEFAULT_MEANING)
            .weight(DEFAULT_WEIGHT)
            .length(DEFAULT_LENGTH)
            .height(DEFAULT_HEIGHT)
            .diet(DEFAULT_DIET)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDt(DEFAULT_CREATED_DT)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .modifiedDt(DEFAULT_MODIFIED_DT);
        // Add required entity
        Epoch epoch;
        if (TestUtil.findAll(em, Epoch.class).isEmpty()) {
            epoch = EpochResourceIT.createEntity(em);
            em.persist(epoch);
            em.flush();
        } else {
            epoch = TestUtil.findAll(em, Epoch.class).get(0);
        }
        dinosaur.setEpochItLived(epoch);
        // Add required entity
        Clade clade;
        if (TestUtil.findAll(em, Clade.class).isEmpty()) {
            clade = CladeResourceIT.createEntity(em);
            em.persist(clade);
            em.flush();
        } else {
            clade = TestUtil.findAll(em, Clade.class).get(0);
        }
        dinosaur.setClade(clade);
        return dinosaur;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dinosaur createUpdatedEntity(EntityManager em) {
        Dinosaur dinosaur = new Dinosaur()
            .name(UPDATED_NAME)
            .pronunciation(UPDATED_PRONUNCIATION)
            .meaning(UPDATED_MEANING)
            .weight(UPDATED_WEIGHT)
            .length(UPDATED_LENGTH)
            .height(UPDATED_HEIGHT)
            .diet(UPDATED_DIET)
            .createdBy(UPDATED_CREATED_BY)
            .createdDt(UPDATED_CREATED_DT)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedDt(UPDATED_MODIFIED_DT);
        // Add required entity
        Epoch epoch;
        if (TestUtil.findAll(em, Epoch.class).isEmpty()) {
            epoch = EpochResourceIT.createUpdatedEntity(em);
            em.persist(epoch);
            em.flush();
        } else {
            epoch = TestUtil.findAll(em, Epoch.class).get(0);
        }
        dinosaur.setEpochItLived(epoch);
        // Add required entity
        Clade clade;
        if (TestUtil.findAll(em, Clade.class).isEmpty()) {
            clade = CladeResourceIT.createUpdatedEntity(em);
            em.persist(clade);
            em.flush();
        } else {
            clade = TestUtil.findAll(em, Clade.class).get(0);
        }
        dinosaur.setClade(clade);
        return dinosaur;
    }

    @BeforeEach
    public void initTest() {
        dinosaur = createEntity(em);
    }

    @Test
    @Transactional
    public void createDinosaur() throws Exception {
        int databaseSizeBeforeCreate = dinosaurRepository.findAll().size();
        // Create the Dinosaur
        DinosaurDTO dinosaurDTO = dinosaurMapper.toDto(dinosaur);
        restDinosaurMockMvc.perform(post("/api/dinosaurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dinosaurDTO)))
            .andExpect(status().isCreated());

        // Validate the Dinosaur in the database
        List<Dinosaur> dinosaurList = dinosaurRepository.findAll();
        assertThat(dinosaurList).hasSize(databaseSizeBeforeCreate + 1);
        Dinosaur testDinosaur = dinosaurList.get(dinosaurList.size() - 1);
        assertThat(testDinosaur.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDinosaur.getPronunciation()).isEqualTo(DEFAULT_PRONUNCIATION);
        assertThat(testDinosaur.getMeaning()).isEqualTo(DEFAULT_MEANING);
        assertThat(testDinosaur.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testDinosaur.getLength()).isEqualTo(DEFAULT_LENGTH);
        assertThat(testDinosaur.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testDinosaur.getDiet()).isEqualTo(DEFAULT_DIET);
        assertThat(testDinosaur.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDinosaur.getCreatedDt()).isEqualTo(DEFAULT_CREATED_DT);
        assertThat(testDinosaur.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
        assertThat(testDinosaur.getModifiedDt()).isEqualTo(DEFAULT_MODIFIED_DT);

        // Validate the Dinosaur in Elasticsearch
        verify(mockDinosaurSearchRepository, times(1)).save(testDinosaur);
    }

    @Test
    @Transactional
    public void createDinosaurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dinosaurRepository.findAll().size();

        // Create the Dinosaur with an existing ID
        dinosaur.setId(1L);
        DinosaurDTO dinosaurDTO = dinosaurMapper.toDto(dinosaur);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDinosaurMockMvc.perform(post("/api/dinosaurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dinosaurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dinosaur in the database
        List<Dinosaur> dinosaurList = dinosaurRepository.findAll();
        assertThat(dinosaurList).hasSize(databaseSizeBeforeCreate);

        // Validate the Dinosaur in Elasticsearch
        verify(mockDinosaurSearchRepository, times(0)).save(dinosaur);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dinosaurRepository.findAll().size();
        // set the field null
        dinosaur.setName(null);

        // Create the Dinosaur, which fails.
        DinosaurDTO dinosaurDTO = dinosaurMapper.toDto(dinosaur);


        restDinosaurMockMvc.perform(post("/api/dinosaurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dinosaurDTO)))
            .andExpect(status().isBadRequest());

        List<Dinosaur> dinosaurList = dinosaurRepository.findAll();
        assertThat(dinosaurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = dinosaurRepository.findAll().size();
        // set the field null
        dinosaur.setCreatedBy(null);

        // Create the Dinosaur, which fails.
        DinosaurDTO dinosaurDTO = dinosaurMapper.toDto(dinosaur);


        restDinosaurMockMvc.perform(post("/api/dinosaurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dinosaurDTO)))
            .andExpect(status().isBadRequest());

        List<Dinosaur> dinosaurList = dinosaurRepository.findAll();
        assertThat(dinosaurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedDtIsRequired() throws Exception {
        int databaseSizeBeforeTest = dinosaurRepository.findAll().size();
        // set the field null
        dinosaur.setCreatedDt(null);

        // Create the Dinosaur, which fails.
        DinosaurDTO dinosaurDTO = dinosaurMapper.toDto(dinosaur);


        restDinosaurMockMvc.perform(post("/api/dinosaurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dinosaurDTO)))
            .andExpect(status().isBadRequest());

        List<Dinosaur> dinosaurList = dinosaurRepository.findAll();
        assertThat(dinosaurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDinosaurs() throws Exception {
        // Initialize the database
        dinosaurRepository.saveAndFlush(dinosaur);

        // Get all the dinosaurList
        restDinosaurMockMvc.perform(get("/api/dinosaurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dinosaur.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].pronunciation").value(hasItem(DEFAULT_PRONUNCIATION)))
            .andExpect(jsonPath("$.[*].meaning").value(hasItem(DEFAULT_MEANING)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].diet").value(hasItem(DEFAULT_DIET.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDt").value(hasItem(DEFAULT_CREATED_DT.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].modifiedDt").value(hasItem(DEFAULT_MODIFIED_DT.toString())));
    }
    
    @Test
    @Transactional
    public void getDinosaur() throws Exception {
        // Initialize the database
        dinosaurRepository.saveAndFlush(dinosaur);

        // Get the dinosaur
        restDinosaurMockMvc.perform(get("/api/dinosaurs/{id}", dinosaur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dinosaur.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.pronunciation").value(DEFAULT_PRONUNCIATION))
            .andExpect(jsonPath("$.meaning").value(DEFAULT_MEANING))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.diet").value(DEFAULT_DIET.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDt").value(DEFAULT_CREATED_DT.toString()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.modifiedDt").value(DEFAULT_MODIFIED_DT.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingDinosaur() throws Exception {
        // Get the dinosaur
        restDinosaurMockMvc.perform(get("/api/dinosaurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDinosaur() throws Exception {
        // Initialize the database
        dinosaurRepository.saveAndFlush(dinosaur);

        int databaseSizeBeforeUpdate = dinosaurRepository.findAll().size();

        // Update the dinosaur
        Dinosaur updatedDinosaur = dinosaurRepository.findById(dinosaur.getId()).get();
        // Disconnect from session so that the updates on updatedDinosaur are not directly saved in db
        em.detach(updatedDinosaur);
        updatedDinosaur
            .name(UPDATED_NAME)
            .pronunciation(UPDATED_PRONUNCIATION)
            .meaning(UPDATED_MEANING)
            .weight(UPDATED_WEIGHT)
            .length(UPDATED_LENGTH)
            .height(UPDATED_HEIGHT)
            .diet(UPDATED_DIET)
            .createdBy(UPDATED_CREATED_BY)
            .createdDt(UPDATED_CREATED_DT)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedDt(UPDATED_MODIFIED_DT);
        DinosaurDTO dinosaurDTO = dinosaurMapper.toDto(updatedDinosaur);

        restDinosaurMockMvc.perform(put("/api/dinosaurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dinosaurDTO)))
            .andExpect(status().isOk());

        // Validate the Dinosaur in the database
        List<Dinosaur> dinosaurList = dinosaurRepository.findAll();
        assertThat(dinosaurList).hasSize(databaseSizeBeforeUpdate);
        Dinosaur testDinosaur = dinosaurList.get(dinosaurList.size() - 1);
        assertThat(testDinosaur.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDinosaur.getPronunciation()).isEqualTo(UPDATED_PRONUNCIATION);
        assertThat(testDinosaur.getMeaning()).isEqualTo(UPDATED_MEANING);
        assertThat(testDinosaur.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testDinosaur.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testDinosaur.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testDinosaur.getDiet()).isEqualTo(UPDATED_DIET);
        assertThat(testDinosaur.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDinosaur.getCreatedDt()).isEqualTo(UPDATED_CREATED_DT);
        assertThat(testDinosaur.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testDinosaur.getModifiedDt()).isEqualTo(UPDATED_MODIFIED_DT);

        // Validate the Dinosaur in Elasticsearch
        verify(mockDinosaurSearchRepository, times(1)).save(testDinosaur);
    }

    @Test
    @Transactional
    public void updateNonExistingDinosaur() throws Exception {
        int databaseSizeBeforeUpdate = dinosaurRepository.findAll().size();

        // Create the Dinosaur
        DinosaurDTO dinosaurDTO = dinosaurMapper.toDto(dinosaur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDinosaurMockMvc.perform(put("/api/dinosaurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dinosaurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dinosaur in the database
        List<Dinosaur> dinosaurList = dinosaurRepository.findAll();
        assertThat(dinosaurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dinosaur in Elasticsearch
        verify(mockDinosaurSearchRepository, times(0)).save(dinosaur);
    }

    @Test
    @Transactional
    public void deleteDinosaur() throws Exception {
        // Initialize the database
        dinosaurRepository.saveAndFlush(dinosaur);

        int databaseSizeBeforeDelete = dinosaurRepository.findAll().size();

        // Delete the dinosaur
        restDinosaurMockMvc.perform(delete("/api/dinosaurs/{id}", dinosaur.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dinosaur> dinosaurList = dinosaurRepository.findAll();
        assertThat(dinosaurList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Dinosaur in Elasticsearch
        verify(mockDinosaurSearchRepository, times(1)).deleteById(dinosaur.getId());
    }

    @Test
    @Transactional
    public void searchDinosaur() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        dinosaurRepository.saveAndFlush(dinosaur);
        when(mockDinosaurSearchRepository.search(queryStringQuery("id:" + dinosaur.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(dinosaur), PageRequest.of(0, 1), 1));

        // Search the dinosaur
        restDinosaurMockMvc.perform(get("/api/_search/dinosaurs?query=id:" + dinosaur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dinosaur.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].pronunciation").value(hasItem(DEFAULT_PRONUNCIATION)))
            .andExpect(jsonPath("$.[*].meaning").value(hasItem(DEFAULT_MEANING)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].diet").value(hasItem(DEFAULT_DIET.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDt").value(hasItem(DEFAULT_CREATED_DT.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].modifiedDt").value(hasItem(DEFAULT_MODIFIED_DT.toString())));
    }
}
