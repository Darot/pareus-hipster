package de.pareus.hiptest.repository.search;

import de.pareus.hiptest.domain.Estate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Estate entity.
 */
public interface EstateSearchRepository extends ElasticsearchRepository<Estate, Long> {
}
