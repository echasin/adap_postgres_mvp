package com.innvo.repository;

import com.innvo.domain.Score;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Score entity.
 */
public interface ScoreRepository extends JpaRepository<Score,Long> {
    
    	Set<Score> findByAssetId(long id); 
    	Page<Score> findByDomain(String domain,Pageable pageable);
    	long countByDomain(String domain);
    	
    	@Query("SELECT MAX(u.lastmodifieddate) FROM Score u WHERE u.route.id=:routeId)")
    	ZonedDateTime findMaxLastmodifieddateByRouteId(@Param("routeId") long routeId);    	
    	@Query("SELECT MAX(u.runid) FROM Score u WHERE u.lastmodifieddate=:lastmodifieddate AND u.route.id=:routeId")
    	long findMaxRunid(@Param("lastmodifieddate") ZonedDateTime lastmodifieddate,@Param("routeId") long routeId);
    	 
    	List<Score> findByRunidAndRouteId(long runid,long routeId);

}
