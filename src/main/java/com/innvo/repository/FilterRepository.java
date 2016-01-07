package com.innvo.repository;

import com.innvo.domain.Filter;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Filter entity.
 */
public interface FilterRepository extends JpaRepository<Filter,Long> {
	
	List<Filter> findByObjrecordtypeObjecttype(String name);

}
