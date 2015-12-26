package com.innvo.repository.search;

import com.innvo.domain.Attackscenario;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Attackscenario entity.
 */
public interface AttackscenarioSearchRepository extends ElasticsearchRepository<Attackscenario, Long> {
}
