package de.pareus.hiptest.repository.search;

import de.pareus.hiptest.domain.Watchlist;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Watchlist entity.
 */
public interface WatchlistSearchRepository extends ElasticsearchRepository<Watchlist, Long> {
}
