package de.pareus.hiptest.repository.search;

import de.pareus.hiptest.domain.EstateAgency;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the EstateAgency entity.
 */
public interface EstateAgencySearchRepository extends ElasticsearchRepository<EstateAgency, Long> {
}
