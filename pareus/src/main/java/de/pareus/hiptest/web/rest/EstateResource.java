package de.pareus.hiptest.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.pareus.hiptest.service.EstateService;
import de.pareus.hiptest.web.rest.errors.BadRequestAlertException;
import de.pareus.hiptest.web.rest.util.HeaderUtil;
import de.pareus.hiptest.web.rest.util.PaginationUtil;
import de.pareus.hiptest.service.dto.EstateDTO;
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
 * REST controller for managing Estate.
 */
@RestController
@RequestMapping("/api")
public class EstateResource {

    private final Logger log = LoggerFactory.getLogger(EstateResource.class);

    private static final String ENTITY_NAME = "estate";

    private final EstateService estateService;

    public EstateResource(EstateService estateService) {
        this.estateService = estateService;
    }

    /**
     * POST  /estates : Create a new estate.
     *
     * @param estateDTO the estateDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new estateDTO, or with status 400 (Bad Request) if the estate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/estates")
    @Timed
    public ResponseEntity<EstateDTO> createEstate(@RequestBody EstateDTO estateDTO) throws URISyntaxException {
        log.debug("REST request to save Estate : {}", estateDTO);
        if (estateDTO.getId() != null) {
            throw new BadRequestAlertException("A new estate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EstateDTO result = estateService.save(estateDTO);
        return ResponseEntity.created(new URI("/api/estates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /estates : Updates an existing estate.
     *
     * @param estateDTO the estateDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated estateDTO,
     * or with status 400 (Bad Request) if the estateDTO is not valid,
     * or with status 500 (Internal Server Error) if the estateDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/estates")
    @Timed
    public ResponseEntity<EstateDTO> updateEstate(@RequestBody EstateDTO estateDTO) throws URISyntaxException {
        log.debug("REST request to update Estate : {}", estateDTO);
        if (estateDTO.getId() == null) {
            return createEstate(estateDTO);
        }
        EstateDTO result = estateService.save(estateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, estateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /estates : get all the estates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of estates in body
     */
    @GetMapping("/estates")
    @Timed
    public ResponseEntity<List<EstateDTO>> getAllEstates(Pageable pageable) {
        log.debug("REST request to get a page of Estates");
        Page<EstateDTO> page = estateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/estates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /estates/:id : get the "id" estate.
     *
     * @param id the id of the estateDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the estateDTO, or with status 404 (Not Found)
     */
    @GetMapping("/estates/{id}")
    @Timed
    public ResponseEntity<EstateDTO> getEstate(@PathVariable Long id) {
        log.debug("REST request to get Estate : {}", id);
        EstateDTO estateDTO = estateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(estateDTO));
    }

    /**
     * DELETE  /estates/:id : delete the "id" estate.
     *
     * @param id the id of the estateDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/estates/{id}")
    @Timed
    public ResponseEntity<Void> deleteEstate(@PathVariable Long id) {
        log.debug("REST request to delete Estate : {}", id);
        estateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/estates?query=:query : search for the estate corresponding
     * to the query.
     *
     * @param query the query of the estate search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/estates")
    @Timed
    public ResponseEntity<List<EstateDTO>> searchEstates(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Estates for query {}", query);
        Page<EstateDTO> page = estateService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/estates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
