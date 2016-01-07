package com.innvo.repository;

import com.innvo.domain.Location;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data JPA repository for the Location entity.
 */
public interface LocationRepository extends JpaRepository<Location,Long> {

      Location findByAssetId(long aseetId);
}
