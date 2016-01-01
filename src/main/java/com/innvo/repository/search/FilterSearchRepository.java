package com.innvo.repository.search;

import com.innvo.domain.Filter;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Filter entity.
 */
public interface FilterSearchRepository extends ElasticsearchRepository<Filter, Long> {
}
