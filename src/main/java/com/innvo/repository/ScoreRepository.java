package com.innvo.repository;

import com.innvo.domain.Score;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.Set;

/**
 * Spring Data JPA repository for the Score entity.
 */
public interface ScoreRepository extends JpaRepository<Score,Long> {
    
    	Set<Score> findByAssetId(long id); 
    	Page<Score> findByDomain(String domain,Pageable pageable);
    	long countByDomain(String domain);

}
