package com.innvo.repository.search;

import com.innvo.domain.Objrecordtype;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Objrecordtype entity.
 */
public interface ObjrecordtypeSearchRepository extends ElasticsearchRepository<Objrecordtype, Long> {
}
