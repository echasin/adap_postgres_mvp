package com.innvo.repository.search;

import com.innvo.domain.Segment;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Segment entity.
 */
public interface SegmentSearchRepository extends ElasticsearchRepository<Segment, Long> {
	
//  @Query(value="{\"sort\": { \"id\" : {\"order\" : \"desc\"}},\"bool\" : {\"match_all\" : {}}}")
//	@Query(value="{\"aggs\":{\"max_id\":{\"max\":{\"field\":\"id\"}}}}")
	@Query(value="{\"bool\":{\"must\":{\"term\":{\"route.id\":\"?0\"}}}}")
	Segment findByRouteIdAndSegmentnumber(long routeId);
}
