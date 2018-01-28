package de.pareus.hiptest.service;

import de.pareus.hiptest.service.dto.EstateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Estate.
 */
public interface EstateService {

    /**
     * Save a estate.
     *
     * @param estateDTO the entity to save
     * @return the persisted entity
     */
    EstateDTO save(EstateDTO estateDTO);

    /**
     * Get all the estates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<EstateDTO> findAll(Pageable pageable);

    /**
     * Get the "id" estate.
     *
     * @param id the id of the entity
     * @return the entity
     */
    EstateDTO findOne(Long id);

    /**
     * Delete the "id" estate.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the estate corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<EstateDTO> search(String query, Pageable pageable);
}
