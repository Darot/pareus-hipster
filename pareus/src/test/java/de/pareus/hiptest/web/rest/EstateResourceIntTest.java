package de.pareus.hiptest.web.rest;

import de.pareus.hiptest.PareusApp;

import de.pareus.hiptest.domain.Estate;
import de.pareus.hiptest.repository.EstateRepository;
import de.pareus.hiptest.service.EstateService;
import de.pareus.hiptest.repository.search.EstateSearchRepository;
import de.pareus.hiptest.service.dto.EstateDTO;
import de.pareus.hiptest.service.mapper.EstateMapper;
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
import java.math.BigDecimal;
import java.util.List;

import static de.pareus.hiptest.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EstateResource REST controller.
 *
 * @see EstateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PareusApp.class)
public class EstateResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    @Autowired
    private EstateRepository estateRepository;

    @Autowired
    private EstateMapper estateMapper;

    @Autowired
    private EstateService estateService;

    @Autowired
    private EstateSearchRepository estateSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEstateMockMvc;

    private Estate estate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EstateResource estateResource = new EstateResource(estateService);
        this.restEstateMockMvc = MockMvcBuilders.standaloneSetup(estateResource)
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
    public static Estate createEntity(EntityManager em) {
        Estate estate = new Estate()
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE);
        return estate;
    }

    @Before
    public void initTest() {
        estateSearchRepository.deleteAll();
        estate = createEntity(em);
    }

    @Test
    @Transactional
    public void createEstate() throws Exception {
        int databaseSizeBeforeCreate = estateRepository.findAll().size();

        // Create the Estate
        EstateDTO estateDTO = estateMapper.toDto(estate);
        restEstateMockMvc.perform(post("/api/estates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estateDTO)))
            .andExpect(status().isCreated());

        // Validate the Estate in the database
        List<Estate> estateList = estateRepository.findAll();
        assertThat(estateList).hasSize(databaseSizeBeforeCreate + 1);
        Estate testEstate = estateList.get(estateList.size() - 1);
        assertThat(testEstate.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEstate.getPrice()).isEqualTo(DEFAULT_PRICE);

        // Validate the Estate in Elasticsearch
        Estate estateEs = estateSearchRepository.findOne(testEstate.getId());
        assertThat(estateEs).isEqualToIgnoringGivenFields(testEstate);
    }

    @Test
    @Transactional
    public void createEstateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = estateRepository.findAll().size();

        // Create the Estate with an existing ID
        estate.setId(1L);
        EstateDTO estateDTO = estateMapper.toDto(estate);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstateMockMvc.perform(post("/api/estates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Estate in the database
        List<Estate> estateList = estateRepository.findAll();
        assertThat(estateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEstates() throws Exception {
        // Initialize the database
        estateRepository.saveAndFlush(estate);

        // Get all the estateList
        restEstateMockMvc.perform(get("/api/estates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estate.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void getEstate() throws Exception {
        // Initialize the database
        estateRepository.saveAndFlush(estate);

        // Get the estate
        restEstateMockMvc.perform(get("/api/estates/{id}", estate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(estate.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEstate() throws Exception {
        // Get the estate
        restEstateMockMvc.perform(get("/api/estates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEstate() throws Exception {
        // Initialize the database
        estateRepository.saveAndFlush(estate);
        estateSearchRepository.save(estate);
        int databaseSizeBeforeUpdate = estateRepository.findAll().size();

        // Update the estate
        Estate updatedEstate = estateRepository.findOne(estate.getId());
        // Disconnect from session so that the updates on updatedEstate are not directly saved in db
        em.detach(updatedEstate);
        updatedEstate
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE);
        EstateDTO estateDTO = estateMapper.toDto(updatedEstate);

        restEstateMockMvc.perform(put("/api/estates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estateDTO)))
            .andExpect(status().isOk());

        // Validate the Estate in the database
        List<Estate> estateList = estateRepository.findAll();
        assertThat(estateList).hasSize(databaseSizeBeforeUpdate);
        Estate testEstate = estateList.get(estateList.size() - 1);
        assertThat(testEstate.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEstate.getPrice()).isEqualTo(UPDATED_PRICE);

        // Validate the Estate in Elasticsearch
        Estate estateEs = estateSearchRepository.findOne(testEstate.getId());
        assertThat(estateEs).isEqualToIgnoringGivenFields(testEstate);
    }

    @Test
    @Transactional
    public void updateNonExistingEstate() throws Exception {
        int databaseSizeBeforeUpdate = estateRepository.findAll().size();

        // Create the Estate
        EstateDTO estateDTO = estateMapper.toDto(estate);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEstateMockMvc.perform(put("/api/estates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estateDTO)))
            .andExpect(status().isCreated());

        // Validate the Estate in the database
        List<Estate> estateList = estateRepository.findAll();
        assertThat(estateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEstate() throws Exception {
        // Initialize the database
        estateRepository.saveAndFlush(estate);
        estateSearchRepository.save(estate);
        int databaseSizeBeforeDelete = estateRepository.findAll().size();

        // Get the estate
        restEstateMockMvc.perform(delete("/api/estates/{id}", estate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean estateExistsInEs = estateSearchRepository.exists(estate.getId());
        assertThat(estateExistsInEs).isFalse();

        // Validate the database is empty
        List<Estate> estateList = estateRepository.findAll();
        assertThat(estateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEstate() throws Exception {
        // Initialize the database
        estateRepository.saveAndFlush(estate);
        estateSearchRepository.save(estate);

        // Search the estate
        restEstateMockMvc.perform(get("/api/_search/estates?query=id:" + estate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estate.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Estate.class);
        Estate estate1 = new Estate();
        estate1.setId(1L);
        Estate estate2 = new Estate();
        estate2.setId(estate1.getId());
        assertThat(estate1).isEqualTo(estate2);
        estate2.setId(2L);
        assertThat(estate1).isNotEqualTo(estate2);
        estate1.setId(null);
        assertThat(estate1).isNotEqualTo(estate2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstateDTO.class);
        EstateDTO estateDTO1 = new EstateDTO();
        estateDTO1.setId(1L);
        EstateDTO estateDTO2 = new EstateDTO();
        assertThat(estateDTO1).isNotEqualTo(estateDTO2);
        estateDTO2.setId(estateDTO1.getId());
        assertThat(estateDTO1).isEqualTo(estateDTO2);
        estateDTO2.setId(2L);
        assertThat(estateDTO1).isNotEqualTo(estateDTO2);
        estateDTO1.setId(null);
        assertThat(estateDTO1).isNotEqualTo(estateDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(estateMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(estateMapper.fromId(null)).isNull();
    }
}
