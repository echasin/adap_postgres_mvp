package com.innvo.repository.search;

import com.innvo.domain.Objclassification;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Objclassification entity.
 */
public interface ObjclassificationSearchRepository extends ElasticsearchRepository<Objclassification, Long> {
}
