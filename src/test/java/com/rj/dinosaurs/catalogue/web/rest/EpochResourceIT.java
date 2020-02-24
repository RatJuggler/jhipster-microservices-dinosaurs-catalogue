package com.rj.dinosaurs.catalogue.web.rest;

import com.rj.dinosaurs.catalogue.CatalogueApp;
import com.rj.dinosaurs.catalogue.domain.Epoch;
import com.rj.dinosaurs.catalogue.repository.EpochRepository;
import com.rj.dinosaurs.catalogue.repository.search.EpochSearchRepository;
import com.rj.dinosaurs.catalogue.service.EpochService;
import com.rj.dinosaurs.catalogue.service.dto.EpochDTO;
import com.rj.dinosaurs.catalogue.service.mapper.EpochMapper;
import com.rj.dinosaurs.catalogue.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static com.rj.dinosaurs.catalogue.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rj.dinosaurs.catalogue.domain.enumeration.Period;
import com.rj.dinosaurs.catalogue.domain.enumeration.EpochRange;
/**
 * Integration tests for the {@link EpochResource} REST controller.
 */
@SpringBootTest(classes = CatalogueApp.class)
public class EpochResourceIT {

    private static final Period DEFAULT_PERIOD = Period.TRIASSIC;
    private static final Period UPDATED_PERIOD = Period.JURASSIC;

    private static final EpochRange DEFAULT_EPOCH = EpochRange.EARLY_LOWER;
    private static final EpochRange UPDATED_EPOCH = EpochRange.MIDDLE;

    private static final Integer DEFAULT_FROM_MA = 0;
    private static final Integer UPDATED_FROM_MA = 1;

    private static final Integer DEFAULT_TO_MA = 0;
    private static final Integer UPDATED_TO_MA = 1;

    @Autowired
    private EpochRepository epochRepository;

    @Autowired
    private EpochMapper epochMapper;

    @Autowired
    private EpochService epochService;

    /**
     * This repository is mocked in the com.rj.dinosaurs.catalogue.repository.search test package.
     *
     * @see com.rj.dinosaurs.catalogue.repository.search.EpochSearchRepositoryMockConfiguration
     */
    @Autowired
    private EpochSearchRepository mockEpochSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restEpochMockMvc;

    private Epoch epoch;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EpochResource epochResource = new EpochResource(epochService);
        this.restEpochMockMvc = MockMvcBuilders.standaloneSetup(epochResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Epoch createEntity(EntityManager em) {
        Epoch epoch = new Epoch()
            .period(DEFAULT_PERIOD)
            .epoch(DEFAULT_EPOCH)
            .fromMa(DEFAULT_FROM_MA)
            .toMa(DEFAULT_TO_MA);
        return epoch;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Epoch createUpdatedEntity(EntityManager em) {
        Epoch epoch = new Epoch()
            .period(UPDATED_PERIOD)
            .epoch(UPDATED_EPOCH)
            .fromMa(UPDATED_FROM_MA)
            .toMa(UPDATED_TO_MA);
        return epoch;
    }

    @BeforeEach
    public void initTest() {
        epoch = createEntity(em);
    }

    @Test
    @Transactional
    public void createEpoch() throws Exception {
        int databaseSizeBeforeCreate = epochRepository.findAll().size();

        // Create the Epoch
        EpochDTO epochDTO = epochMapper.toDto(epoch);
        restEpochMockMvc.perform(post("/api/epoches")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(epochDTO)))
            .andExpect(status().isCreated());

        // Validate the Epoch in the database
        List<Epoch> epochList = epochRepository.findAll();
        assertThat(epochList).hasSize(databaseSizeBeforeCreate + 1);
        Epoch testEpoch = epochList.get(epochList.size() - 1);
        assertThat(testEpoch.getPeriod()).isEqualTo(DEFAULT_PERIOD);
        assertThat(testEpoch.getEpoch()).isEqualTo(DEFAULT_EPOCH);
        assertThat(testEpoch.getFromMa()).isEqualTo(DEFAULT_FROM_MA);
        assertThat(testEpoch.getToMa()).isEqualTo(DEFAULT_TO_MA);

        // Validate the Epoch in Elasticsearch
        verify(mockEpochSearchRepository, times(1)).save(testEpoch);
    }

    @Test
    @Transactional
    public void createEpochWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = epochRepository.findAll().size();

        // Create the Epoch with an existing ID
        epoch.setId(1L);
        EpochDTO epochDTO = epochMapper.toDto(epoch);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEpochMockMvc.perform(post("/api/epoches")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(epochDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Epoch in the database
        List<Epoch> epochList = epochRepository.findAll();
        assertThat(epochList).hasSize(databaseSizeBeforeCreate);

        // Validate the Epoch in Elasticsearch
        verify(mockEpochSearchRepository, times(0)).save(epoch);
    }


    @Test
    @Transactional
    public void checkPeriodIsRequired() throws Exception {
        int databaseSizeBeforeTest = epochRepository.findAll().size();
        // set the field null
        epoch.setPeriod(null);

        // Create the Epoch, which fails.
        EpochDTO epochDTO = epochMapper.toDto(epoch);

        restEpochMockMvc.perform(post("/api/epoches")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(epochDTO)))
            .andExpect(status().isBadRequest());

        List<Epoch> epochList = epochRepository.findAll();
        assertThat(epochList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEpochIsRequired() throws Exception {
        int databaseSizeBeforeTest = epochRepository.findAll().size();
        // set the field null
        epoch.setEpoch(null);

        // Create the Epoch, which fails.
        EpochDTO epochDTO = epochMapper.toDto(epoch);

        restEpochMockMvc.perform(post("/api/epoches")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(epochDTO)))
            .andExpect(status().isBadRequest());

        List<Epoch> epochList = epochRepository.findAll();
        assertThat(epochList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFromMaIsRequired() throws Exception {
        int databaseSizeBeforeTest = epochRepository.findAll().size();
        // set the field null
        epoch.setFromMa(null);

        // Create the Epoch, which fails.
        EpochDTO epochDTO = epochMapper.toDto(epoch);

        restEpochMockMvc.perform(post("/api/epoches")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(epochDTO)))
            .andExpect(status().isBadRequest());

        List<Epoch> epochList = epochRepository.findAll();
        assertThat(epochList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkToMaIsRequired() throws Exception {
        int databaseSizeBeforeTest = epochRepository.findAll().size();
        // set the field null
        epoch.setToMa(null);

        // Create the Epoch, which fails.
        EpochDTO epochDTO = epochMapper.toDto(epoch);

        restEpochMockMvc.perform(post("/api/epoches")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(epochDTO)))
            .andExpect(status().isBadRequest());

        List<Epoch> epochList = epochRepository.findAll();
        assertThat(epochList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEpoches() throws Exception {
        // Initialize the database
        epochRepository.saveAndFlush(epoch);

        // Get all the epochList
        restEpochMockMvc.perform(get("/api/epoches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(epoch.getId().intValue())))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD.toString())))
            .andExpect(jsonPath("$.[*].epoch").value(hasItem(DEFAULT_EPOCH.toString())))
            .andExpect(jsonPath("$.[*].fromMa").value(hasItem(DEFAULT_FROM_MA)))
            .andExpect(jsonPath("$.[*].toMa").value(hasItem(DEFAULT_TO_MA)));
    }
    
    @Test
    @Transactional
    public void getEpoch() throws Exception {
        // Initialize the database
        epochRepository.saveAndFlush(epoch);

        // Get the epoch
        restEpochMockMvc.perform(get("/api/epoches/{id}", epoch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(epoch.getId().intValue()))
            .andExpect(jsonPath("$.period").value(DEFAULT_PERIOD.toString()))
            .andExpect(jsonPath("$.epoch").value(DEFAULT_EPOCH.toString()))
            .andExpect(jsonPath("$.fromMa").value(DEFAULT_FROM_MA))
            .andExpect(jsonPath("$.toMa").value(DEFAULT_TO_MA));
    }

    @Test
    @Transactional
    public void getNonExistingEpoch() throws Exception {
        // Get the epoch
        restEpochMockMvc.perform(get("/api/epoches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEpoch() throws Exception {
        // Initialize the database
        epochRepository.saveAndFlush(epoch);

        int databaseSizeBeforeUpdate = epochRepository.findAll().size();

        // Update the epoch
        Epoch updatedEpoch = epochRepository.findById(epoch.getId()).get();
        // Disconnect from session so that the updates on updatedEpoch are not directly saved in db
        em.detach(updatedEpoch);
        updatedEpoch
            .period(UPDATED_PERIOD)
            .epoch(UPDATED_EPOCH)
            .fromMa(UPDATED_FROM_MA)
            .toMa(UPDATED_TO_MA);
        EpochDTO epochDTO = epochMapper.toDto(updatedEpoch);

        restEpochMockMvc.perform(put("/api/epoches")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(epochDTO)))
            .andExpect(status().isOk());

        // Validate the Epoch in the database
        List<Epoch> epochList = epochRepository.findAll();
        assertThat(epochList).hasSize(databaseSizeBeforeUpdate);
        Epoch testEpoch = epochList.get(epochList.size() - 1);
        assertThat(testEpoch.getPeriod()).isEqualTo(UPDATED_PERIOD);
        assertThat(testEpoch.getEpoch()).isEqualTo(UPDATED_EPOCH);
        assertThat(testEpoch.getFromMa()).isEqualTo(UPDATED_FROM_MA);
        assertThat(testEpoch.getToMa()).isEqualTo(UPDATED_TO_MA);

        // Validate the Epoch in Elasticsearch
        verify(mockEpochSearchRepository, times(1)).save(testEpoch);
    }

    @Test
    @Transactional
    public void updateNonExistingEpoch() throws Exception {
        int databaseSizeBeforeUpdate = epochRepository.findAll().size();

        // Create the Epoch
        EpochDTO epochDTO = epochMapper.toDto(epoch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEpochMockMvc.perform(put("/api/epoches")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(epochDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Epoch in the database
        List<Epoch> epochList = epochRepository.findAll();
        assertThat(epochList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Epoch in Elasticsearch
        verify(mockEpochSearchRepository, times(0)).save(epoch);
    }

    @Test
    @Transactional
    public void deleteEpoch() throws Exception {
        // Initialize the database
        epochRepository.saveAndFlush(epoch);

        int databaseSizeBeforeDelete = epochRepository.findAll().size();

        // Delete the epoch
        restEpochMockMvc.perform(delete("/api/epoches/{id}", epoch.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Epoch> epochList = epochRepository.findAll();
        assertThat(epochList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Epoch in Elasticsearch
        verify(mockEpochSearchRepository, times(1)).deleteById(epoch.getId());
    }

    @Test
    @Transactional
    public void searchEpoch() throws Exception {
        // Initialize the database
        epochRepository.saveAndFlush(epoch);
        when(mockEpochSearchRepository.search(queryStringQuery("id:" + epoch.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(epoch), PageRequest.of(0, 1), 1));
        // Search the epoch
        restEpochMockMvc.perform(get("/api/_search/epoches?query=id:" + epoch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(epoch.getId().intValue())))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD.toString())))
            .andExpect(jsonPath("$.[*].epoch").value(hasItem(DEFAULT_EPOCH.toString())))
            .andExpect(jsonPath("$.[*].fromMa").value(hasItem(DEFAULT_FROM_MA)))
            .andExpect(jsonPath("$.[*].toMa").value(hasItem(DEFAULT_TO_MA)));
    }
}
