package com.innvo.repository;

import com.innvo.domain.Segment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Segment entity.
 */
public interface SegmentRepository extends JpaRepository<Segment,Long> {

}
