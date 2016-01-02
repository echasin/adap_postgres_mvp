package com.innvo.repository;

import com.innvo.domain.Segment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Segment entity.
 */
public interface SegmentRepository extends JpaRepository<Segment,Long> {
	
	 Page<Segment> findByDomain(String domain,Pageable pageable);
 	 long countByDomain(String domain); 

}
