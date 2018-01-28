package de.pareus.hiptest.service;

import de.pareus.hiptest.service.dto.EstateAgencyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing EstateAgency.
 */
public interface EstateAgencyService {

    /**
     * Save a estateAgency.
     *
     * @param estateAgencyDTO the entity to save
     * @return the persisted entity
     */
    EstateAgencyDTO save(EstateAgencyDTO estateAgencyDTO);

    /**
     * Get all the estateAgencies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<EstateAgencyDTO> findAll(Pageable pageable);

    /**
     * Get the "id" estateAgency.
     *
     * @param id the id of the entity
     * @return the entity
     */
    EstateAgencyDTO findOne(Long id);

    /**
     * Delete the "id" estateAgency.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the estateAgency corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<EstateAgencyDTO> search(String query, Pageable pageable);
}
