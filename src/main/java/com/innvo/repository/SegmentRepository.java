package com.innvo.repository;

import com.innvo.domain.Segment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Segment entity.
 */
public interface SegmentRepository extends JpaRepository<Segment,Long> {
	
	 Page<Segment> findByDomain(String domain,Pageable pageable);
	 @Query("SELECT u FROM Segment u WHERE u.segmentnumber = (SELECT MAX(u.segmentnumber) FROM Segment u) AND u.route.id=:routeId")
	 Segment findByRouteIdAndSegmentnumber(@Param("routeId") long routeId);
 	 long countByDomain(String domain); 

}
