package com.innvo.repository;

import com.innvo.domain.Objcategory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Objcategory entity.
 */
public interface ObjcategoryRepository extends JpaRepository<Objcategory,Long> {
	List<Objcategory> findByObjclassificationIdAndDomain(long id,String domain); 
}
