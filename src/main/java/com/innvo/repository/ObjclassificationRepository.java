package com.innvo.repository;

import com.innvo.domain.Objclassification;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Objclassification entity.
 */
public interface ObjclassificationRepository extends JpaRepository<Objclassification,Long> {

}
