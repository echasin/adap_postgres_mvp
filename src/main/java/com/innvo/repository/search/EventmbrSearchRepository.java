package com.innvo.repository.search;

import com.innvo.domain.Eventmbr;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Eventmbr entity.
 */
public interface EventmbrSearchRepository extends ElasticsearchRepository<Eventmbr, Long> {
}
