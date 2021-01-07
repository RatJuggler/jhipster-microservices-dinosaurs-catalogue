package com.rj.dinosaurs.catalogue.web.rest;

import com.rj.dinosaurs.catalogue.CatalogueApp;
import com.rj.dinosaurs.catalogue.domain.Clade;
import com.rj.dinosaurs.catalogue.repository.CladeRepository;
import com.rj.dinosaurs.catalogue.repository.search.CladeSearchRepository;
import com.rj.dinosaurs.catalogue.service.CladeService;
import com.rj.dinosaurs.catalogue.service.dto.CladeDTO;
import com.rj.dinosaurs.catalogue.service.mapper.CladeMapper;

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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CladeResource} REST controller.
 */
@SpringBootTest(classes = CatalogueApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CladeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRONUNCIATION = "AAAAAAAAAA";
    private static final String UPDATED_PRONUNCIATION = "BBBBBBBBBB";

    private static final String DEFAULT_MEANING = "AAAAAAAAAA";
    private static final String UPDATED_MEANING = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private CladeRepository cladeRepository;

    @Autowired
    private CladeMapper cladeMapper;

    @Autowired
    private CladeService cladeService;

    /**
     * This repository is mocked in the com.rj.dinosaurs.catalogue.repository.search test package.
     *
     * @see com.rj.dinosaurs.catalogue.repository.search.CladeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CladeSearchRepository mockCladeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCladeMockMvc;

    private Clade clade;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clade createEntity(EntityManager em) {
        Clade clade = new Clade()
            .name(DEFAULT_NAME)
            .pronunciation(DEFAULT_PRONUNCIATION)
            .meaning(DEFAULT_MEANING)
            .description(DEFAULT_DESCRIPTION);
        return clade;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clade createUpdatedEntity(EntityManager em) {
        Clade clade = new Clade()
            .name(UPDATED_NAME)
            .pronunciation(UPDATED_PRONUNCIATION)
            .meaning(UPDATED_MEANING)
            .description(UPDATED_DESCRIPTION);
        return clade;
    }

    @BeforeEach
    public void initTest() {
        clade = createEntity(em);
    }

    @Test
    @Transactional
    public void createClade() throws Exception {
        int databaseSizeBeforeCreate = cladeRepository.findAll().size();
        // Create the Clade
        CladeDTO cladeDTO = cladeMapper.toDto(clade);
        restCladeMockMvc.perform(post("/api/clades")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cladeDTO)))
            .andExpect(status().isCreated());

        // Validate the Clade in the database
        List<Clade> cladeList = cladeRepository.findAll();
        assertThat(cladeList).hasSize(databaseSizeBeforeCreate + 1);
        Clade testClade = cladeList.get(cladeList.size() - 1);
        assertThat(testClade.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClade.getPronunciation()).isEqualTo(DEFAULT_PRONUNCIATION);
        assertThat(testClade.getMeaning()).isEqualTo(DEFAULT_MEANING);
        assertThat(testClade.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Clade in Elasticsearch
        verify(mockCladeSearchRepository, times(1)).save(testClade);
    }

    @Test
    @Transactional
    public void createCladeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cladeRepository.findAll().size();

        // Create the Clade with an existing ID
        clade.setId(1L);
        CladeDTO cladeDTO = cladeMapper.toDto(clade);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCladeMockMvc.perform(post("/api/clades")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cladeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Clade in the database
        List<Clade> cladeList = cladeRepository.findAll();
        assertThat(cladeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Clade in Elasticsearch
        verify(mockCladeSearchRepository, times(0)).save(clade);
    }


    @Test
    @Transactional
    public void getAllClades() throws Exception {
        // Initialize the database
        cladeRepository.saveAndFlush(clade);

        // Get all the cladeList
        restCladeMockMvc.perform(get("/api/clades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clade.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].pronunciation").value(hasItem(DEFAULT_PRONUNCIATION)))
            .andExpect(jsonPath("$.[*].meaning").value(hasItem(DEFAULT_MEANING)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getClade() throws Exception {
        // Initialize the database
        cladeRepository.saveAndFlush(clade);

        // Get the clade
        restCladeMockMvc.perform(get("/api/clades/{id}", clade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clade.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.pronunciation").value(DEFAULT_PRONUNCIATION))
            .andExpect(jsonPath("$.meaning").value(DEFAULT_MEANING))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingClade() throws Exception {
        // Get the clade
        restCladeMockMvc.perform(get("/api/clades/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClade() throws Exception {
        // Initialize the database
        cladeRepository.saveAndFlush(clade);

        int databaseSizeBeforeUpdate = cladeRepository.findAll().size();

        // Update the clade
        Clade updatedClade = cladeRepository.findById(clade.getId()).get();
        // Disconnect from session so that the updates on updatedClade are not directly saved in db
        em.detach(updatedClade);
        updatedClade
            .name(UPDATED_NAME)
            .pronunciation(UPDATED_PRONUNCIATION)
            .meaning(UPDATED_MEANING)
            .description(UPDATED_DESCRIPTION);
        CladeDTO cladeDTO = cladeMapper.toDto(updatedClade);

        restCladeMockMvc.perform(put("/api/clades")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cladeDTO)))
            .andExpect(status().isOk());

        // Validate the Clade in the database
        List<Clade> cladeList = cladeRepository.findAll();
        assertThat(cladeList).hasSize(databaseSizeBeforeUpdate);
        Clade testClade = cladeList.get(cladeList.size() - 1);
        assertThat(testClade.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClade.getPronunciation()).isEqualTo(UPDATED_PRONUNCIATION);
        assertThat(testClade.getMeaning()).isEqualTo(UPDATED_MEANING);
        assertThat(testClade.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Clade in Elasticsearch
        verify(mockCladeSearchRepository, times(1)).save(testClade);
    }

    @Test
    @Transactional
    public void updateNonExistingClade() throws Exception {
        int databaseSizeBeforeUpdate = cladeRepository.findAll().size();

        // Create the Clade
        CladeDTO cladeDTO = cladeMapper.toDto(clade);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCladeMockMvc.perform(put("/api/clades")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cladeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Clade in the database
        List<Clade> cladeList = cladeRepository.findAll();
        assertThat(cladeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Clade in Elasticsearch
        verify(mockCladeSearchRepository, times(0)).save(clade);
    }

    @Test
    @Transactional
    public void deleteClade() throws Exception {
        // Initialize the database
        cladeRepository.saveAndFlush(clade);

        int databaseSizeBeforeDelete = cladeRepository.findAll().size();

        // Delete the clade
        restCladeMockMvc.perform(delete("/api/clades/{id}", clade.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Clade> cladeList = cladeRepository.findAll();
        assertThat(cladeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Clade in Elasticsearch
        verify(mockCladeSearchRepository, times(1)).deleteById(clade.getId());
    }

    @Test
    @Transactional
    public void searchClade() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        cladeRepository.saveAndFlush(clade);
        when(mockCladeSearchRepository.search(queryStringQuery("id:" + clade.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(clade), PageRequest.of(0, 1), 1));

        // Search the clade
        restCladeMockMvc.perform(get("/api/_search/clades?query=id:" + clade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clade.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].pronunciation").value(hasItem(DEFAULT_PRONUNCIATION)))
            .andExpect(jsonPath("$.[*].meaning").value(hasItem(DEFAULT_MEANING)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
