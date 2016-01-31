package com.innvo.repository;

import com.innvo.domain.Event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Event entity.
 */
public interface EventRepository extends JpaRepository<Event,Long> {

	 Page<Event> findByDomainAndAssetId(String domain,long id,Pageable pageable);
	 
	 long countByDomainAndAssetId(String domain,long id);

	 long countByDomain(String domain);
	 
	 long countByAssetId(long id);
}
