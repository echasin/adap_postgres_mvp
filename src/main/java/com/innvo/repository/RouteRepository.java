package com.innvo.repository;

import com.innvo.domain.Route;
import com.innvo.domain.Segment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Route entity.
 */
public interface RouteRepository extends JpaRepository<Route,Long> {

	Page<Route> findByDomain(String domain,Pageable pageable);
	long countByDomain(String domain);
}
