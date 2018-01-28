package de.pareus.hiptest.service.impl;

import de.pareus.hiptest.service.EstateAgencyService;
import de.pareus.hiptest.domain.EstateAgency;
import de.pareus.hiptest.repository.EstateAgencyRepository;
import de.pareus.hiptest.repository.search.EstateAgencySearchRepository;
import de.pareus.hiptest.service.dto.EstateAgencyDTO;
import de.pareus.hiptest.service.mapper.EstateAgencyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing EstateAgency.
 */
@Service
@Transactional
public class EstateAgencyServiceImpl implements EstateAgencyService {

    private final Logger log = LoggerFactory.getLogger(EstateAgencyServiceImpl.class);

    private final EstateAgencyRepository estateAgencyRepository;

    private final EstateAgencyMapper estateAgencyMapper;

    private final EstateAgencySearchRepository estateAgencySearchRepository;

    public EstateAgencyServiceImpl(EstateAgencyRepository estateAgencyRepository, EstateAgencyMapper estateAgencyMapper, EstateAgencySearchRepository estateAgencySearchRepository) {
        this.estateAgencyRepository = estateAgencyRepository;
        this.estateAgencyMapper = estateAgencyMapper;
        this.estateAgencySearchRepository = estateAgencySearchRepository;
    }

    /**
     * Save a estateAgency.
     *
     * @param estateAgencyDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EstateAgencyDTO save(EstateAgencyDTO estateAgencyDTO) {
        log.debug("Request to save EstateAgency : {}", estateAgencyDTO);
        EstateAgency estateAgency = estateAgencyMapper.toEntity(estateAgencyDTO);
        estateAgency = estateAgencyRepository.save(estateAgency);
        EstateAgencyDTO result = estateAgencyMapper.toDto(estateAgency);
        estateAgencySearchRepository.save(estateAgency);
        return result;
    }

    /**
     * Get all the estateAgencies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EstateAgencyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EstateAgencies");
        return estateAgencyRepository.findAll(pageable)
            .map(estateAgencyMapper::toDto);
    }

    /**
     * Get one estateAgency by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public EstateAgencyDTO findOne(Long id) {
        log.debug("Request to get EstateAgency : {}", id);
        EstateAgency estateAgency = estateAgencyRepository.findOne(id);
        return estateAgencyMapper.toDto(estateAgency);
    }

    /**
     * Delete the estateAgency by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EstateAgency : {}", id);
        estateAgencyRepository.delete(id);
        estateAgencySearchRepository.delete(id);
    }

    /**
     * Search for the estateAgency corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EstateAgencyDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of EstateAgencies for query {}", query);
        Page<EstateAgency> result = estateAgencySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(estateAgencyMapper::toDto);
    }
}
