package de.pareus.hiptest.service.impl;

import de.pareus.hiptest.service.EstateService;
import de.pareus.hiptest.domain.Estate;
import de.pareus.hiptest.repository.EstateRepository;
import de.pareus.hiptest.repository.search.EstateSearchRepository;
import de.pareus.hiptest.service.dto.EstateDTO;
import de.pareus.hiptest.service.mapper.EstateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Estate.
 */
@Service
@Transactional
public class EstateServiceImpl implements EstateService {

    private final Logger log = LoggerFactory.getLogger(EstateServiceImpl.class);

    private final EstateRepository estateRepository;

    private final EstateMapper estateMapper;

    private final EstateSearchRepository estateSearchRepository;

    public EstateServiceImpl(EstateRepository estateRepository, EstateMapper estateMapper, EstateSearchRepository estateSearchRepository) {
        this.estateRepository = estateRepository;
        this.estateMapper = estateMapper;
        this.estateSearchRepository = estateSearchRepository;
    }

    /**
     * Save a estate.
     *
     * @param estateDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EstateDTO save(EstateDTO estateDTO) {
        log.debug("Request to save Estate : {}", estateDTO);
        Estate estate = estateMapper.toEntity(estateDTO);
        estate = estateRepository.save(estate);
        EstateDTO result = estateMapper.toDto(estate);
        estateSearchRepository.save(estate);
        return result;
    }

    /**
     * Get all the estates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EstateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Estates");
        return estateRepository.findAll(pageable)
            .map(estateMapper::toDto);
    }

    /**
     * Get one estate by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public EstateDTO findOne(Long id) {
        log.debug("Request to get Estate : {}", id);
        Estate estate = estateRepository.findOne(id);
        return estateMapper.toDto(estate);
    }

    /**
     * Delete the estate by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Estate : {}", id);
        estateRepository.delete(id);
        estateSearchRepository.delete(id);
    }

    /**
     * Search for the estate corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EstateDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Estates for query {}", query);
        Page<Estate> result = estateSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(estateMapper::toDto);
    }
}
