package de.pareus.hiptest.web.rest;

import de.pareus.hiptest.PareusApp;

import de.pareus.hiptest.domain.Watchlist;
import de.pareus.hiptest.repository.WatchlistRepository;
import de.pareus.hiptest.service.WatchlistService;
import de.pareus.hiptest.repository.search.WatchlistSearchRepository;
import de.pareus.hiptest.service.dto.WatchlistDTO;
import de.pareus.hiptest.service.mapper.WatchlistMapper;
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
 * Test class for the WatchlistResource REST controller.
 *
 * @see WatchlistResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PareusApp.class)
public class WatchlistResourceIntTest {

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private WatchlistMapper watchlistMapper;

    @Autowired
    private WatchlistService watchlistService;

    @Autowired
    private WatchlistSearchRepository watchlistSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWatchlistMockMvc;

    private Watchlist watchlist;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WatchlistResource watchlistResource = new WatchlistResource(watchlistService);
        this.restWatchlistMockMvc = MockMvcBuilders.standaloneSetup(watchlistResource)
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
    public static Watchlist createEntity(EntityManager em) {
        Watchlist watchlist = new Watchlist();
        return watchlist;
    }

    @Before
    public void initTest() {
        watchlistSearchRepository.deleteAll();
        watchlist = createEntity(em);
    }

    @Test
    @Transactional
    public void createWatchlist() throws Exception {
        int databaseSizeBeforeCreate = watchlistRepository.findAll().size();

        // Create the Watchlist
        WatchlistDTO watchlistDTO = watchlistMapper.toDto(watchlist);
        restWatchlistMockMvc.perform(post("/api/watchlists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(watchlistDTO)))
            .andExpect(status().isCreated());

        // Validate the Watchlist in the database
        List<Watchlist> watchlistList = watchlistRepository.findAll();
        assertThat(watchlistList).hasSize(databaseSizeBeforeCreate + 1);
        Watchlist testWatchlist = watchlistList.get(watchlistList.size() - 1);

        // Validate the Watchlist in Elasticsearch
        Watchlist watchlistEs = watchlistSearchRepository.findOne(testWatchlist.getId());
        assertThat(watchlistEs).isEqualToIgnoringGivenFields(testWatchlist);
    }

    @Test
    @Transactional
    public void createWatchlistWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = watchlistRepository.findAll().size();

        // Create the Watchlist with an existing ID
        watchlist.setId(1L);
        WatchlistDTO watchlistDTO = watchlistMapper.toDto(watchlist);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWatchlistMockMvc.perform(post("/api/watchlists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(watchlistDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Watchlist in the database
        List<Watchlist> watchlistList = watchlistRepository.findAll();
        assertThat(watchlistList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWatchlists() throws Exception {
        // Initialize the database
        watchlistRepository.saveAndFlush(watchlist);

        // Get all the watchlistList
        restWatchlistMockMvc.perform(get("/api/watchlists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(watchlist.getId().intValue())));
    }

    @Test
    @Transactional
    public void getWatchlist() throws Exception {
        // Initialize the database
        watchlistRepository.saveAndFlush(watchlist);

        // Get the watchlist
        restWatchlistMockMvc.perform(get("/api/watchlists/{id}", watchlist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(watchlist.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWatchlist() throws Exception {
        // Get the watchlist
        restWatchlistMockMvc.perform(get("/api/watchlists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWatchlist() throws Exception {
        // Initialize the database
        watchlistRepository.saveAndFlush(watchlist);
        watchlistSearchRepository.save(watchlist);
        int databaseSizeBeforeUpdate = watchlistRepository.findAll().size();

        // Update the watchlist
        Watchlist updatedWatchlist = watchlistRepository.findOne(watchlist.getId());
        // Disconnect from session so that the updates on updatedWatchlist are not directly saved in db
        em.detach(updatedWatchlist);
        WatchlistDTO watchlistDTO = watchlistMapper.toDto(updatedWatchlist);

        restWatchlistMockMvc.perform(put("/api/watchlists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(watchlistDTO)))
            .andExpect(status().isOk());

        // Validate the Watchlist in the database
        List<Watchlist> watchlistList = watchlistRepository.findAll();
        assertThat(watchlistList).hasSize(databaseSizeBeforeUpdate);
        Watchlist testWatchlist = watchlistList.get(watchlistList.size() - 1);

        // Validate the Watchlist in Elasticsearch
        Watchlist watchlistEs = watchlistSearchRepository.findOne(testWatchlist.getId());
        assertThat(watchlistEs).isEqualToIgnoringGivenFields(testWatchlist);
    }

    @Test
    @Transactional
    public void updateNonExistingWatchlist() throws Exception {
        int databaseSizeBeforeUpdate = watchlistRepository.findAll().size();

        // Create the Watchlist
        WatchlistDTO watchlistDTO = watchlistMapper.toDto(watchlist);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWatchlistMockMvc.perform(put("/api/watchlists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(watchlistDTO)))
            .andExpect(status().isCreated());

        // Validate the Watchlist in the database
        List<Watchlist> watchlistList = watchlistRepository.findAll();
        assertThat(watchlistList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWatchlist() throws Exception {
        // Initialize the database
        watchlistRepository.saveAndFlush(watchlist);
        watchlistSearchRepository.save(watchlist);
        int databaseSizeBeforeDelete = watchlistRepository.findAll().size();

        // Get the watchlist
        restWatchlistMockMvc.perform(delete("/api/watchlists/{id}", watchlist.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean watchlistExistsInEs = watchlistSearchRepository.exists(watchlist.getId());
        assertThat(watchlistExistsInEs).isFalse();

        // Validate the database is empty
        List<Watchlist> watchlistList = watchlistRepository.findAll();
        assertThat(watchlistList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWatchlist() throws Exception {
        // Initialize the database
        watchlistRepository.saveAndFlush(watchlist);
        watchlistSearchRepository.save(watchlist);

        // Search the watchlist
        restWatchlistMockMvc.perform(get("/api/_search/watchlists?query=id:" + watchlist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(watchlist.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Watchlist.class);
        Watchlist watchlist1 = new Watchlist();
        watchlist1.setId(1L);
        Watchlist watchlist2 = new Watchlist();
        watchlist2.setId(watchlist1.getId());
        assertThat(watchlist1).isEqualTo(watchlist2);
        watchlist2.setId(2L);
        assertThat(watchlist1).isNotEqualTo(watchlist2);
        watchlist1.setId(null);
        assertThat(watchlist1).isNotEqualTo(watchlist2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WatchlistDTO.class);
        WatchlistDTO watchlistDTO1 = new WatchlistDTO();
        watchlistDTO1.setId(1L);
        WatchlistDTO watchlistDTO2 = new WatchlistDTO();
        assertThat(watchlistDTO1).isNotEqualTo(watchlistDTO2);
        watchlistDTO2.setId(watchlistDTO1.getId());
        assertThat(watchlistDTO1).isEqualTo(watchlistDTO2);
        watchlistDTO2.setId(2L);
        assertThat(watchlistDTO1).isNotEqualTo(watchlistDTO2);
        watchlistDTO1.setId(null);
        assertThat(watchlistDTO1).isNotEqualTo(watchlistDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(watchlistMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(watchlistMapper.fromId(null)).isNull();
    }
}
