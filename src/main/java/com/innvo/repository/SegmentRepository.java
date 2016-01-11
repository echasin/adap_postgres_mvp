package com.innvo.repository;

import com.innvo.domain.Segment;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Segment entity.
 */
public interface SegmentRepository extends JpaRepository<Segment,Long> {
	
	 Page<Segment> findByDomain(String domain,Pageable pageable);
	 @Query("SELECT MAX(u.segmentnumber) FROM Segment u WHERE u.route.id=:routeId")
	 long getMaxSegmentnumberByRouteId(@Param("routeId") long routeId);
	 @Query("SELECT MIN(u.segmentnumber) FROM Segment u WHERE u.route.id=:routeId")
	 long getMinSegmentnumberByRouteId(@Param("routeId") long routeId);
	 Segment findByRouteIdAndSegmentnumber(long routeId,long segmentNumber);
	 List<Segment> findByRouteId(long routeId);
 	 long countByDomain(String domain); 

}
