package de.pareus.hiptest.repository.search;

import de.pareus.hiptest.domain.Image;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Image entity.
 */
public interface ImageSearchRepository extends ElasticsearchRepository<Image, Long> {
}
