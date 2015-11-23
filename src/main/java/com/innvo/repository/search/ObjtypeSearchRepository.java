package com.innvo.repository.search;

import com.innvo.domain.Objtype;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Objtype entity.
 */
public interface ObjtypeSearchRepository extends ElasticsearchRepository<Objtype, Long> {
}
