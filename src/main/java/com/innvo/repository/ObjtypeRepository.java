package com.innvo.repository;

import com.innvo.domain.Objtype;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Objtype entity.
 */
public interface ObjtypeRepository extends JpaRepository<Objtype,Long> {

}
