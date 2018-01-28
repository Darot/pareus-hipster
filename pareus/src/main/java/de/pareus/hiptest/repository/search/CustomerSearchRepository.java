package de.pareus.hiptest.repository.search;

import de.pareus.hiptest.domain.Customer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Customer entity.
 */
public interface CustomerSearchRepository extends ElasticsearchRepository<Customer, Long> {
}
