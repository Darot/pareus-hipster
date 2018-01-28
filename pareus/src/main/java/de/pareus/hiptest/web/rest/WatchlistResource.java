package de.pareus.hiptest.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.pareus.hiptest.service.WatchlistService;
import de.pareus.hiptest.web.rest.errors.BadRequestAlertException;
import de.pareus.hiptest.web.rest.util.HeaderUtil;
import de.pareus.hiptest.web.rest.util.PaginationUtil;
import de.pareus.hiptest.service.dto.WatchlistDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Watchlist.
 */
@RestController
@RequestMapping("/api")
public class WatchlistResource {

    private final Logger log = LoggerFactory.getLogger(WatchlistResource.class);

    private static final String ENTITY_NAME = "watchlist";

    private final WatchlistService watchlistService;

    public WatchlistResource(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    /**
     * POST  /watchlists : Create a new watchlist.
     *
     * @param watchlistDTO the watchlistDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new watchlistDTO, or with status 400 (Bad Request) if the watchlist has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/watchlists")
    @Timed
    public ResponseEntity<WatchlistDTO> createWatchlist(@RequestBody WatchlistDTO watchlistDTO) throws URISyntaxException {
        log.debug("REST request to save Watchlist : {}", watchlistDTO);
        if (watchlistDTO.getId() != null) {
            throw new BadRequestAlertException("A new watchlist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WatchlistDTO result = watchlistService.save(watchlistDTO);
        return ResponseEntity.created(new URI("/api/watchlists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /watchlists : Updates an existing watchlist.
     *
     * @param watchlistDTO the watchlistDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated watchlistDTO,
     * or with status 400 (Bad Request) if the watchlistDTO is not valid,
     * or with status 500 (Internal Server Error) if the watchlistDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/watchlists")
    @Timed
    public ResponseEntity<WatchlistDTO> updateWatchlist(@RequestBody WatchlistDTO watchlistDTO) throws URISyntaxException {
        log.debug("REST request to update Watchlist : {}", watchlistDTO);
        if (watchlistDTO.getId() == null) {
            return createWatchlist(watchlistDTO);
        }
        WatchlistDTO result = watchlistService.save(watchlistDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, watchlistDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /watchlists : get all the watchlists.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of watchlists in body
     */
    @GetMapping("/watchlists")
    @Timed
    public ResponseEntity<List<WatchlistDTO>> getAllWatchlists(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("customer-is-null".equals(filter)) {
            log.debug("REST request to get all Watchlists where customer is null");
            return new ResponseEntity<>(watchlistService.findAllWhereCustomerIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of Watchlists");
        Page<WatchlistDTO> page = watchlistService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/watchlists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /watchlists/:id : get the "id" watchlist.
     *
     * @param id the id of the watchlistDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the watchlistDTO, or with status 404 (Not Found)
     */
    @GetMapping("/watchlists/{id}")
    @Timed
    public ResponseEntity<WatchlistDTO> getWatchlist(@PathVariable Long id) {
        log.debug("REST request to get Watchlist : {}", id);
        WatchlistDTO watchlistDTO = watchlistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(watchlistDTO));
    }

    /**
     * DELETE  /watchlists/:id : delete the "id" watchlist.
     *
     * @param id the id of the watchlistDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/watchlists/{id}")
    @Timed
    public ResponseEntity<Void> deleteWatchlist(@PathVariable Long id) {
        log.debug("REST request to delete Watchlist : {}", id);
        watchlistService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/watchlists?query=:query : search for the watchlist corresponding
     * to the query.
     *
     * @param query the query of the watchlist search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/watchlists")
    @Timed
    public ResponseEntity<List<WatchlistDTO>> searchWatchlists(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Watchlists for query {}", query);
        Page<WatchlistDTO> page = watchlistService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/watchlists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
