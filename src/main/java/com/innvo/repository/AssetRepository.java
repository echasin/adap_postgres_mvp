package com.innvo.repository;

import com.innvo.domain.Asset;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the Asset entity.
 */
public interface AssetRepository extends JpaRepository<Asset,Long> {
    	
	    Page<Asset> findByDomain(String domain,Pageable pageable);
    	long countByDomain(String domain);
}
