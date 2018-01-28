package de.pareus.hiptest.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.pareus.hiptest.service.EstateAgencyService;
import de.pareus.hiptest.web.rest.errors.BadRequestAlertException;
import de.pareus.hiptest.web.rest.util.HeaderUtil;
import de.pareus.hiptest.web.rest.util.PaginationUtil;
import de.pareus.hiptest.service.dto.EstateAgencyDTO;
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
 * REST controller for managing EstateAgency.
 */
@RestController
@RequestMapping("/api")
public class EstateAgencyResource {

    private final Logger log = LoggerFactory.getLogger(EstateAgencyResource.class);

    private static final String ENTITY_NAME = "estateAgency";

    private final EstateAgencyService estateAgencyService;

    public EstateAgencyResource(EstateAgencyService estateAgencyService) {
        this.estateAgencyService = estateAgencyService;
    }

    /**
     * POST  /estate-agencies : Create a new estateAgency.
     *
     * @param estateAgencyDTO the estateAgencyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new estateAgencyDTO, or with status 400 (Bad Request) if the estateAgency has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/estate-agencies")
    @Timed
    public ResponseEntity<EstateAgencyDTO> createEstateAgency(@RequestBody EstateAgencyDTO estateAgencyDTO) throws URISyntaxException {
        log.debug("REST request to save EstateAgency : {}", estateAgencyDTO);
        if (estateAgencyDTO.getId() != null) {
            throw new BadRequestAlertException("A new estateAgency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EstateAgencyDTO result = estateAgencyService.save(estateAgencyDTO);
        return ResponseEntity.created(new URI("/api/estate-agencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /estate-agencies : Updates an existing estateAgency.
     *
     * @param estateAgencyDTO the estateAgencyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated estateAgencyDTO,
     * or with status 400 (Bad Request) if the estateAgencyDTO is not valid,
     * or with status 500 (Internal Server Error) if the estateAgencyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/estate-agencies")
    @Timed
    public ResponseEntity<EstateAgencyDTO> updateEstateAgency(@RequestBody EstateAgencyDTO estateAgencyDTO) throws URISyntaxException {
        log.debug("REST request to update EstateAgency : {}", estateAgencyDTO);
        if (estateAgencyDTO.getId() == null) {
            return createEstateAgency(estateAgencyDTO);
        }
        EstateAgencyDTO result = estateAgencyService.save(estateAgencyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, estateAgencyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /estate-agencies : get all the estateAgencies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of estateAgencies in body
     */
    @GetMapping("/estate-agencies")
    @Timed
    public ResponseEntity<List<EstateAgencyDTO>> getAllEstateAgencies(Pageable pageable) {
        log.debug("REST request to get a page of EstateAgencies");
        Page<EstateAgencyDTO> page = estateAgencyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/estate-agencies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /estate-agencies/:id : get the "id" estateAgency.
     *
     * @param id the id of the estateAgencyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the estateAgencyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/estate-agencies/{id}")
    @Timed
    public ResponseEntity<EstateAgencyDTO> getEstateAgency(@PathVariable Long id) {
        log.debug("REST request to get EstateAgency : {}", id);
        EstateAgencyDTO estateAgencyDTO = estateAgencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(estateAgencyDTO));
    }

    /**
     * DELETE  /estate-agencies/:id : delete the "id" estateAgency.
     *
     * @param id the id of the estateAgencyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/estate-agencies/{id}")
    @Timed
    public ResponseEntity<Void> deleteEstateAgency(@PathVariable Long id) {
        log.debug("REST request to delete EstateAgency : {}", id);
        estateAgencyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/estate-agencies?query=:query : search for the estateAgency corresponding
     * to the query.
     *
     * @param query the query of the estateAgency search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/estate-agencies")
    @Timed
    public ResponseEntity<List<EstateAgencyDTO>> searchEstateAgencies(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of EstateAgencies for query {}", query);
        Page<EstateAgencyDTO> page = estateAgencyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/estate-agencies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
