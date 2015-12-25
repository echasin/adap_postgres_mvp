package com.innvo.repository.search;

import com.innvo.domain.Segment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Segment entity.
 */
public interface SegmentSearchRepository extends ElasticsearchRepository<Segment, Long> {
}
