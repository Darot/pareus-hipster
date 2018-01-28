package de.pareus.hiptest.service.impl;

import de.pareus.hiptest.service.WatchlistService;
import de.pareus.hiptest.domain.Watchlist;
import de.pareus.hiptest.repository.WatchlistRepository;
import de.pareus.hiptest.repository.search.WatchlistSearchRepository;
import de.pareus.hiptest.service.dto.WatchlistDTO;
import de.pareus.hiptest.service.mapper.WatchlistMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Watchlist.
 */
@Service
@Transactional
public class WatchlistServiceImpl implements WatchlistService {

    private final Logger log = LoggerFactory.getLogger(WatchlistServiceImpl.class);

    private final WatchlistRepository watchlistRepository;

    private final WatchlistMapper watchlistMapper;

    private final WatchlistSearchRepository watchlistSearchRepository;

    public WatchlistServiceImpl(WatchlistRepository watchlistRepository, WatchlistMapper watchlistMapper, WatchlistSearchRepository watchlistSearchRepository) {
        this.watchlistRepository = watchlistRepository;
        this.watchlistMapper = watchlistMapper;
        this.watchlistSearchRepository = watchlistSearchRepository;
    }

    /**
     * Save a watchlist.
     *
     * @param watchlistDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public WatchlistDTO save(WatchlistDTO watchlistDTO) {
        log.debug("Request to save Watchlist : {}", watchlistDTO);
        Watchlist watchlist = watchlistMapper.toEntity(watchlistDTO);
        watchlist = watchlistRepository.save(watchlist);
        WatchlistDTO result = watchlistMapper.toDto(watchlist);
        watchlistSearchRepository.save(watchlist);
        return result;
    }

    /**
     * Get all the watchlists.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WatchlistDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Watchlists");
        return watchlistRepository.findAll(pageable)
            .map(watchlistMapper::toDto);
    }


    /**
     *  get all the watchlists where Customer is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<WatchlistDTO> findAllWhereCustomerIsNull() {
        log.debug("Request to get all watchlists where Customer is null");
        return StreamSupport
            .stream(watchlistRepository.findAll().spliterator(), false)
            .filter(watchlist -> watchlist.getCustomer() == null)
            .map(watchlistMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one watchlist by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public WatchlistDTO findOne(Long id) {
        log.debug("Request to get Watchlist : {}", id);
        Watchlist watchlist = watchlistRepository.findOne(id);
        return watchlistMapper.toDto(watchlist);
    }

    /**
     * Delete the watchlist by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Watchlist : {}", id);
        watchlistRepository.delete(id);
        watchlistSearchRepository.delete(id);
    }

    /**
     * Search for the watchlist corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WatchlistDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Watchlists for query {}", query);
        Page<Watchlist> result = watchlistSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(watchlistMapper::toDto);
    }
}
