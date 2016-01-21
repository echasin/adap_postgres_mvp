package com.innvo.repository;

import com.innvo.domain.Location;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Location entity.
 */
public interface LocationRepository extends JpaRepository<Location,Long> {

	 @Query("SELECT u FROM Location u WHERE u.asset.id=:assetId")
     Set<Location> findByAssetIds(@Param("assetId")long aseetId);

	 @Query("SELECT u FROM Location u WHERE u.asset.id=:assetId")
     List<Location> findByAssets(@Param("assetId")long aseetId);

	 Location findByAssetId(long aseetId);
	 
	 Page<Location> findByDomain(String domain,Pageable pageable);
	 
	 long countByDomain(String domain);
	 
	 public Location findByAssetIdAndIsprimary(long id,boolean isprimary);
}
