package com.innvo.repository;

import com.innvo.domain.Scorefactor;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Scorefactor entity.
 */
public interface ScorefactorRepository extends JpaRepository<Scorefactor,Long> {

}
