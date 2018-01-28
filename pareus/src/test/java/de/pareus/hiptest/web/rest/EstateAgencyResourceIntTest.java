package de.pareus.hiptest.web.rest;

import de.pareus.hiptest.PareusApp;

import de.pareus.hiptest.domain.EstateAgency;
import de.pareus.hiptest.repository.EstateAgencyRepository;
import de.pareus.hiptest.service.EstateAgencyService;
import de.pareus.hiptest.repository.search.EstateAgencySearchRepository;
import de.pareus.hiptest.service.dto.EstateAgencyDTO;
import de.pareus.hiptest.service.mapper.EstateAgencyMapper;
import de.pareus.hiptest.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static de.pareus.hiptest.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EstateAgencyResource REST controller.
 *
 * @see EstateAgencyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PareusApp.class)
public class EstateAgencyResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private EstateAgencyRepository estateAgencyRepository;

    @Autowired
    private EstateAgencyMapper estateAgencyMapper;

    @Autowired
    private EstateAgencyService estateAgencyService;

    @Autowired
    private EstateAgencySearchRepository estateAgencySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEstateAgencyMockMvc;

    private EstateAgency estateAgency;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EstateAgencyResource estateAgencyResource = new EstateAgencyResource(estateAgencyService);
        this.restEstateAgencyMockMvc = MockMvcBuilders.standaloneSetup(estateAgencyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EstateAgency createEntity(EntityManager em) {
        EstateAgency estateAgency = new EstateAgency()
            .name(DEFAULT_NAME);
        return estateAgency;
    }

    @Before
    public void initTest() {
        estateAgencySearchRepository.deleteAll();
        estateAgency = createEntity(em);
    }

    @Test
    @Transactional
    public void createEstateAgency() throws Exception {
        int databaseSizeBeforeCreate = estateAgencyRepository.findAll().size();

        // Create the EstateAgency
        EstateAgencyDTO estateAgencyDTO = estateAgencyMapper.toDto(estateAgency);
        restEstateAgencyMockMvc.perform(post("/api/estate-agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estateAgencyDTO)))
            .andExpect(status().isCreated());

        // Validate the EstateAgency in the database
        List<EstateAgency> estateAgencyList = estateAgencyRepository.findAll();
        assertThat(estateAgencyList).hasSize(databaseSizeBeforeCreate + 1);
        EstateAgency testEstateAgency = estateAgencyList.get(estateAgencyList.size() - 1);
        assertThat(testEstateAgency.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the EstateAgency in Elasticsearch
        EstateAgency estateAgencyEs = estateAgencySearchRepository.findOne(testEstateAgency.getId());
        assertThat(estateAgencyEs).isEqualToIgnoringGivenFields(testEstateAgency);
    }

    @Test
    @Transactional
    public void createEstateAgencyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = estateAgencyRepository.findAll().size();

        // Create the EstateAgency with an existing ID
        estateAgency.setId(1L);
        EstateAgencyDTO estateAgencyDTO = estateAgencyMapper.toDto(estateAgency);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstateAgencyMockMvc.perform(post("/api/estate-agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estateAgencyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EstateAgency in the database
        List<EstateAgency> estateAgencyList = estateAgencyRepository.findAll();
        assertThat(estateAgencyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEstateAgencies() throws Exception {
        // Initialize the database
        estateAgencyRepository.saveAndFlush(estateAgency);

        // Get all the estateAgencyList
        restEstateAgencyMockMvc.perform(get("/api/estate-agencies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estateAgency.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getEstateAgency() throws Exception {
        // Initialize the database
        estateAgencyRepository.saveAndFlush(estateAgency);

        // Get the estateAgency
        restEstateAgencyMockMvc.perform(get("/api/estate-agencies/{id}", estateAgency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(estateAgency.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEstateAgency() throws Exception {
        // Get the estateAgency
        restEstateAgencyMockMvc.perform(get("/api/estate-agencies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEstateAgency() throws Exception {
        // Initialize the database
        estateAgencyRepository.saveAndFlush(estateAgency);
        estateAgencySearchRepository.save(estateAgency);
        int databaseSizeBeforeUpdate = estateAgencyRepository.findAll().size();

        // Update the estateAgency
        EstateAgency updatedEstateAgency = estateAgencyRepository.findOne(estateAgency.getId());
        // Disconnect from session so that the updates on updatedEstateAgency are not directly saved in db
        em.detach(updatedEstateAgency);
        updatedEstateAgency
            .name(UPDATED_NAME);
        EstateAgencyDTO estateAgencyDTO = estateAgencyMapper.toDto(updatedEstateAgency);

        restEstateAgencyMockMvc.perform(put("/api/estate-agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estateAgencyDTO)))
            .andExpect(status().isOk());

        // Validate the EstateAgency in the database
        List<EstateAgency> estateAgencyList = estateAgencyRepository.findAll();
        assertThat(estateAgencyList).hasSize(databaseSizeBeforeUpdate);
        EstateAgency testEstateAgency = estateAgencyList.get(estateAgencyList.size() - 1);
        assertThat(testEstateAgency.getName()).isEqualTo(UPDATED_NAME);

        // Validate the EstateAgency in Elasticsearch
        EstateAgency estateAgencyEs = estateAgencySearchRepository.findOne(testEstateAgency.getId());
        assertThat(estateAgencyEs).isEqualToIgnoringGivenFields(testEstateAgency);
    }

    @Test
    @Transactional
    public void updateNonExistingEstateAgency() throws Exception {
        int databaseSizeBeforeUpdate = estateAgencyRepository.findAll().size();

        // Create the EstateAgency
        EstateAgencyDTO estateAgencyDTO = estateAgencyMapper.toDto(estateAgency);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEstateAgencyMockMvc.perform(put("/api/estate-agencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estateAgencyDTO)))
            .andExpect(status().isCreated());

        // Validate the EstateAgency in the database
        List<EstateAgency> estateAgencyList = estateAgencyRepository.findAll();
        assertThat(estateAgencyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEstateAgency() throws Exception {
        // Initialize the database
        estateAgencyRepository.saveAndFlush(estateAgency);
        estateAgencySearchRepository.save(estateAgency);
        int databaseSizeBeforeDelete = estateAgencyRepository.findAll().size();

        // Get the estateAgency
        restEstateAgencyMockMvc.perform(delete("/api/estate-agencies/{id}", estateAgency.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean estateAgencyExistsInEs = estateAgencySearchRepository.exists(estateAgency.getId());
        assertThat(estateAgencyExistsInEs).isFalse();

        // Validate the database is empty
        List<EstateAgency> estateAgencyList = estateAgencyRepository.findAll();
        assertThat(estateAgencyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEstateAgency() throws Exception {
        // Initialize the database
        estateAgencyRepository.saveAndFlush(estateAgency);
        estateAgencySearchRepository.save(estateAgency);

        // Search the estateAgency
        restEstateAgencyMockMvc.perform(get("/api/_search/estate-agencies?query=id:" + estateAgency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estateAgency.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstateAgency.class);
        EstateAgency estateAgency1 = new EstateAgency();
        estateAgency1.setId(1L);
        EstateAgency estateAgency2 = new EstateAgency();
        estateAgency2.setId(estateAgency1.getId());
        assertThat(estateAgency1).isEqualTo(estateAgency2);
        estateAgency2.setId(2L);
        assertThat(estateAgency1).isNotEqualTo(estateAgency2);
        estateAgency1.setId(null);
        assertThat(estateAgency1).isNotEqualTo(estateAgency2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstateAgencyDTO.class);
        EstateAgencyDTO estateAgencyDTO1 = new EstateAgencyDTO();
        estateAgencyDTO1.setId(1L);
        EstateAgencyDTO estateAgencyDTO2 = new EstateAgencyDTO();
        assertThat(estateAgencyDTO1).isNotEqualTo(estateAgencyDTO2);
        estateAgencyDTO2.setId(estateAgencyDTO1.getId());
        assertThat(estateAgencyDTO1).isEqualTo(estateAgencyDTO2);
        estateAgencyDTO2.setId(2L);
        assertThat(estateAgencyDTO1).isNotEqualTo(estateAgencyDTO2);
        estateAgencyDTO1.setId(null);
        assertThat(estateAgencyDTO1).isNotEqualTo(estateAgencyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(estateAgencyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(estateAgencyMapper.fromId(null)).isNull();
    }
}
