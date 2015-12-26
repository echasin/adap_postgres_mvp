package com.innvo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.innvo.domain.Asset;
import com.innvo.domain.Filter;
import com.innvo.domain.Location;
import com.innvo.domain.Score;
import com.innvo.domain.User;
import com.innvo.repository.AssetRepository;
import com.innvo.repository.FilterRepository;
import com.innvo.repository.LocationRepository;
import com.innvo.repository.ScoreRepository;
import com.innvo.repository.UserRepository;
import com.innvo.repository.search.AssetSearchRepository;
import com.innvo.web.rest.util.HeaderUtil;
import com.innvo.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.servlet.http.HttpServletRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;

import static org.elasticsearch.index.query.QueryBuilders.*;
import org.elasticsearch.index.query.WrapperQueryBuilder;
import org.joda.time.DateTime;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * REST controller for managing Asset.
 */
@RestController
@RequestMapping("/api")
public class AssetResource {

    private final Logger log = LoggerFactory.getLogger(AssetResource.class);

    @Inject
    private AssetRepository assetRepository;

    @Inject
    private AssetSearchRepository assetSearchRepository;

    @Inject
    FilterRepository filterRepository;

    @Inject
    ElasticsearchTemplate elasticsearchTemplate;

    @Inject
    UserRepository userRepository;

    @Inject
    private LocationRepository locationRepository;

    @Inject
    private ScoreRepository scoreRepository;

    /**
     * POST /assets -> Create a new asset.
     */
    @RequestMapping(value = "/assets",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Asset> createAsset(@Valid @RequestBody Asset asset, Principal principal) throws URISyntaxException {
        log.debug("REST request to save Asset : {}", asset);
        if (asset.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new asset cannot already have an ID").body(null);
        }
        DateTime lastmodifieddate = new DateTime();
        User user = userRepository.findByLogin(principal.getName());
        asset.setDomain(user.getDomain());
        //asset.setStatus("Active");
        asset.setLastmodifiedby(principal.getName());
        //asset.setLastmodifieddate(lastmodifieddate);
        Asset result = assetRepository.save(asset);
        assetSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/assets/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("asset", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT /assets -> Updates an existing asset.
     */
    @RequestMapping(value = "/assets",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Asset> updateAsset(@Valid @RequestBody Asset asset, Principal principal) throws URISyntaxException {
        log.debug("REST request to update Asset : {}", asset);
        if (asset.getId() == null) {
            return createAsset(asset, principal);
        }
        DateTime lastmodifieddate = new DateTime();
        User user = userRepository.findByLogin(principal.getName());
        asset.setDomain(user.getDomain());
        //asset.setStatus("Active");
        asset.setLastmodifiedby(principal.getName());
        //asset.setLastmodifieddate(lastmodifieddate);
        Asset result = assetRepository.save(asset);
        assetSearchRepository.save(asset);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("asset", asset.getId().toString()))
                .body(result);
    }

    /**
     * GET /assets -> get all the assets.
     */
    @RequestMapping(value = "/assets/{paginationOptions.pageNumber}/{paginationOptions.pageSize}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Asset>> getAllAssets(HttpServletRequest request, Principal principal, @PathVariable("paginationOptions.pageNumber") String pageNumber,
            @PathVariable("paginationOptions.pageSize") String pageSize
    )
            throws URISyntaxException {
        int thepage = Integer.parseInt(pageNumber);
        int thepagesize = Integer.parseInt(pageSize);
        User user = userRepository.findByLogin(principal.getName());
        PageRequest pageRequest = new PageRequest(thepage, thepagesize, Sort.Direction.ASC, "id");
        Page<Asset> data = assetRepository.findByDomain(user.getDomain(), pageRequest);
        return new ResponseEntity<>(data.getContent(), HttpStatus.OK);
    }

    /**
     * GET /assets/count -> Get Records Size
     */
    @RequestMapping(value = "/recordsLength",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public long getlength(Principal principal)
            throws URISyntaxException {
        User user = userRepository.findByLogin(principal.getName());
        long length = assetRepository.countByDomain(user.getDomain());
        return length;
    }

    /**
     * GET /assets/:id -> get the "id" asset.
     */
    @RequestMapping(value = "/assets/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Asset> getAsset(@PathVariable Long id) {
        log.debug("REST request to get Asset : {}", id);
        return Optional.ofNullable(assetRepository.findOne(id))
                .map(asset -> new ResponseEntity<>(
                                asset,
                                HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET /assets_h/:id -> get the "id" asset.
     */
    @RequestMapping(value = "/assets_h/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Asset> getAsset_h(@PathVariable Long id) {
        log.debug("REST request to get Asset : {}", id);
        Asset object = assetRepository.findOne(id);
        Set<Location> locations = locationRepository.findByAssetId(id);
        Set<Score> scores = scoreRepository.findByAssetId(id);
        object.setLocations(locations);
        object.setScores(scores);
        return Optional.ofNullable(object)
                .map(asset -> new ResponseEntity<>(
                                asset,
                                HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE /assets/:id -> delete the "id" asset.
     */
    @RequestMapping(value = "/assets/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        log.debug("REST request to delete Asset : {}", id);
        assetRepository.delete(id);
        assetSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("asset", id.toString())).build();
    }

    /**
     * SEARCH /_search/assets/:query -> search for the asset corresponding to
     * the query.
     */
    @RequestMapping(value = "/_search/assets/{query}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Asset> searchAssets(@PathVariable String query) {
        return StreamSupport
                .stream(assetSearchRepository.search(queryStringQuery(query)).spliterator(), false)
                .collect(Collectors.toList());
    }
    
       
    /**
     * GET -> index objects.
     */
    @RequestMapping(value = "indexasset",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void add() {
        List<Asset> assets = assetRepository.findAll();
        log.debug("In IndexResource.java");

        for (Asset asset:assets) {
            String id = asset.getId().toString();
            IndexQuery indexQuery = new IndexQueryBuilder().withId(id).withObject(asset).build();
            elasticsearchTemplate.index(indexQuery);
        }
    }
    
    /**
     * GET ->Execute filter asset.
     */
    @RequestMapping(value = "executefilter/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Asset> elastic(HttpServletRequest request,@PathVariable long id) {
    	Filter filter=filterRepository.findOne(id);
    	String query=filter.getQueryelastic();
    	BoolQueryBuilder bool = new BoolQueryBuilder();
        bool.must(new WrapperQueryBuilder(query));

    		List<Asset> result = Lists.newArrayList(assetSearchRepository.search(bool));
    		return result;
    }

    /**
     * GET -> Edit filter asset.
     */
    @RequestMapping(value = "editfilter/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Filter editFilter(@PathVariable long id) {
 
    	Filter filter=filterRepository.findOne(id); 		
    		return filter;
    }
    
}
