package de.pareus.hiptest.service;

import de.pareus.hiptest.service.dto.WatchlistDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Watchlist.
 */
public interface WatchlistService {

    /**
     * Save a watchlist.
     *
     * @param watchlistDTO the entity to save
     * @return the persisted entity
     */
    WatchlistDTO save(WatchlistDTO watchlistDTO);

    /**
     * Get all the watchlists.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<WatchlistDTO> findAll(Pageable pageable);
    /**
     * Get all the WatchlistDTO where Customer is null.
     *
     * @return the list of entities
     */
    List<WatchlistDTO> findAllWhereCustomerIsNull();

    /**
     * Get the "id" watchlist.
     *
     * @param id the id of the entity
     * @return the entity
     */
    WatchlistDTO findOne(Long id);

    /**
     * Delete the "id" watchlist.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the watchlist corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<WatchlistDTO> search(String query, Pageable pageable);
}
