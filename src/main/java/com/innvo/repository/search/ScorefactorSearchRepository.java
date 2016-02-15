package com.innvo.repository.search;

import com.innvo.domain.Scorefactor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Scorefactor entity.
 */
public interface ScorefactorSearchRepository extends ElasticsearchRepository<Scorefactor, Long> {
}
