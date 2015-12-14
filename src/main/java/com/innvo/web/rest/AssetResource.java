package com.innvo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.innvo.domain.Asset;
import com.innvo.domain.Location;
import com.innvo.domain.Score;
import com.innvo.repository.AssetRepository;
import com.innvo.repository.LocationRepository;
import com.innvo.repository.ScoreRepository;
import com.innvo.repository.search.AssetSearchRepository;
import com.innvo.web.rest.util.HeaderUtil;
import com.innvo.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.servlet.http.HttpServletRequest;

import static org.elasticsearch.index.query.QueryBuilders.*;
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
    public ResponseEntity<Asset> createAsset(@Valid @RequestBody Asset asset) throws URISyntaxException {
        log.debug("REST request to save Asset : {}", asset);
        if (asset.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new asset cannot already have an ID").body(null);
        }
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
    public ResponseEntity<Asset> updateAsset(@Valid @RequestBody Asset asset) throws URISyntaxException {
        log.debug("REST request to update Asset : {}", asset);
        if (asset.getId() == null) {
            return createAsset(asset);
        }
        Asset result = assetRepository.save(asset);
        assetSearchRepository.save(asset);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("asset", asset.getId().toString()))
                .body(result);
    }

    /**
     * GET /assets -> get all the assets to get the 2500 record(you can change
     * the number by angular)this is spring pagination sorted by id asc.
     */
    @RequestMapping(value = "/assets/{paginationOptions.pageNumber}/{paginationOptions.pageSize}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Asset>> getAllAssets(HttpServletRequest request,
            @PathVariable("paginationOptions.pageNumber") String pageNumber,
            @PathVariable("paginationOptions.pageSize") String pageSize
    )
            throws URISyntaxException {
        int thepage = Integer.parseInt(pageNumber);
        int thepagesize = Integer.parseInt(pageSize);
        PageRequest pageRequest = new PageRequest(thepage, thepagesize, Sort.Direction.ASC, "id");
        Page<Asset> list = assetRepository.findAll(pageRequest);
        return new ResponseEntity<>(list.getContent(), HttpStatus.OK);
    }

    /**
     * @RequestMapping(value = "/assets", method = RequestMethod.GET, produces =
     * MediaType.APPLICATION_JSON_VALUE)
     * @Timed public ResponseEntity<List<Asset>> getAllAssets(Pageable pageable)
     * throws URISyntaxException { Page<Asset> page =
     * assetRepository.findAll(pageable); HttpHeaders headers =
     * PaginationUtil.generatePaginationHttpHeaders(page, "/api/assets"); return
     * new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK); }
     */
    /**
     * GET /assets/recordslength -> get length of all records in database.
     */
    @RequestMapping(value = "/asset/recordsLength",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public int getlength()
            throws URISyntaxException {
        int length = assetRepository.findAll().size();
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
}
