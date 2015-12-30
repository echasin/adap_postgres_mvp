package com.innvo.repository;

import com.innvo.domain.Attackscenario;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Attackscenario entity.
 */
public interface AttackscenarioRepository extends JpaRepository<Attackscenario,Long> {

}
