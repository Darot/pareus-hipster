package de.pareus.hiptest.repository;

import de.pareus.hiptest.domain.Watchlist;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Watchlist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

}
