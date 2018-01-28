package de.pareus.hiptest.repository;

import de.pareus.hiptest.domain.EstateAgency;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the EstateAgency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstateAgencyRepository extends JpaRepository<EstateAgency, Long> {

}
