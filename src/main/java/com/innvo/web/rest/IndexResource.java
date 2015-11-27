package com.innvo.web.rest;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.innvo.domain.Asset;
import com.innvo.domain.Location;
import com.innvo.domain.Objcategory;
import com.innvo.domain.Objclassification;
import com.innvo.domain.Objrecordtype;
import com.innvo.domain.Objtype;
import com.innvo.repository.AssetRepository;
import com.innvo.repository.LocationRepository;
import com.innvo.repository.ObjcategoryRepository;
import com.innvo.repository.ObjclassificationRepository;
import com.innvo.repository.ObjrecordtypeRepository;
import com.innvo.repository.ObjtypeRepository;
import com.innvo.repository.UserRepository;
import com.innvo.repository.search.AssetSearchRepository;
import com.innvo.service.UserService;

/**
 * REST controller for managing index.
 */
@RestController
@RequestMapping("/api")
public class IndexResource {

    private final Logger log = LoggerFactory.getLogger(AssetResource.class);

    @Inject
    ElasticsearchTemplate elasticsearchTemplate;

    @Inject
    ObjclassificationRepository objclassificationRepository;

    @Inject
    ObjrecordtypeRepository objrecordtypeRepository;

    @Inject
    ObjcategoryRepository objcategoryRepository;

    @Inject
    ObjtypeRepository objtypeRepository;

    //@Inject
    //MetricRepository metricRepository;
    
    @Inject
    private AssetRepository assetRepository;

    @Inject
    LocationRepository locationRepository;

    /**
     * GET -> index objects.
     */
    @RequestMapping(value = "index",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void add() {
        
        List<Objrecordtype> objrecordtypes = objrecordtypeRepository.findAll();
        List<Objclassification> objclassifications = objclassificationRepository.findAll();
        List<Objcategory> objcategorys = objcategoryRepository.findAll();
        List<Objtype> objtypes = objtypeRepository.findAll();
        //List<Metric> metrics = metricRepository.findAll();
        List<Asset> assets = assetRepository.findAll();
        List<Location> locations = locationRepository.findAll();

        log.debug("In IndexResource.java");

        for (Asset asset : assets) {
            String id = asset.getId().toString();
            IndexQuery indexQuery = new IndexQueryBuilder().withId(id).withObject(asset).build();
            elasticsearchTemplate.index(indexQuery);
        }
        for (Location location : locations) {
            String id = location.getId().toString();
            IndexQuery indexQuery = new IndexQueryBuilder().withId(id).withObject(location).build();
            elasticsearchTemplate.index(indexQuery);
        }

        for (Objrecordtype objrecordtype : objrecordtypes) {
            String id = objrecordtype.getId().toString();
            IndexQuery indexQuery = new IndexQueryBuilder().withId(id).withObject(objrecordtype).build();
            elasticsearchTemplate.index(indexQuery);
        }
        for (Objclassification objclassification : objclassifications) {
            String id = objclassification.getId().toString();
            IndexQuery indexQuery = new IndexQueryBuilder().withId(id).withObject(objclassification).build();
            elasticsearchTemplate.index(indexQuery);
        }
        for (Objcategory objcategory : objcategorys) {
            String id = objcategory.getId().toString();
            IndexQuery indexQuery = new IndexQueryBuilder().withId(id).withObject(objcategory).build();
            elasticsearchTemplate.index(indexQuery);
        }
        for (Objtype objType : objtypes) {
            String id = objType.getId().toString();
            IndexQuery indexQuery = new IndexQueryBuilder().withId(id).withObject(objType).build();
            elasticsearchTemplate.index(indexQuery);
        }

    }
}
