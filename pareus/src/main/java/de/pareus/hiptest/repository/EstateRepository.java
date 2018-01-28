package de.pareus.hiptest.repository;

import de.pareus.hiptest.domain.Estate;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Estate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstateRepository extends JpaRepository<Estate, Long> {

}
