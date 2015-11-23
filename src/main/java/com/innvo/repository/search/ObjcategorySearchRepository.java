package com.innvo.repository.search;

import com.innvo.domain.Objcategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Objcategory entity.
 */
public interface ObjcategorySearchRepository extends ElasticsearchRepository<Objcategory, Long> {
}
