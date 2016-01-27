package com.innvo.repository;

import com.innvo.domain.Eventmbr;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Eventmbr entity.
 */
public interface EventmbrRepository extends JpaRepository<Eventmbr,Long> {

}
