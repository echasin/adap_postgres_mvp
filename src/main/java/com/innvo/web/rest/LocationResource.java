package com.innvo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.innvo.domain.Location;
import com.innvo.domain.Route;
import com.innvo.domain.Score;
import com.innvo.domain.User;
import com.innvo.domain.enumeration.Status;
import com.innvo.repository.LocationRepository;
import com.innvo.repository.UserRepository;
import com.innvo.repository.search.LocationSearchRepository;
import com.innvo.web.rest.util.HeaderUtil;
import com.innvo.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Location.
 */
@RestController
@RequestMapping("/api")
public class LocationResource {

    private final Logger log = LoggerFactory.getLogger(LocationResource.class);

    @Inject
    private LocationRepository locationRepository;

    @Inject
    private LocationSearchRepository locationSearchRepository;

    @Inject 
    ElasticsearchTemplate elasticsearchTemplate;
    
    @Inject
    UserRepository userRepository;
    /**
     * POST  /locations -> Create a new location.
     */
    @RequestMapping(value = "/locations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Location> createLocation(@Valid @RequestBody Location location,Principal principal) throws URISyntaxException {
        log.debug("REST request to save Location : {}", location);
        if (location.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new location cannot already have an ID").body(null);
        }
        ZonedDateTime lastmodifieddate = ZonedDateTime.now(ZoneId.systemDefault());
        User user = userRepository.findByLogin(principal.getName());
        location.setDomain(user.getDomain());
        location.setStatus(Status.Active);
        location.setLastmodifiedby(principal.getName());
        location.setLastmodifieddate(lastmodifieddate);
        Location result = locationRepository.save(location);
        locationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("location", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /locations -> Updates an existing location.
     */
    @RequestMapping(value = "/locations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Location> updateLocation(@Valid @RequestBody Location location,Principal principal) throws URISyntaxException {
        log.debug("REST request to update Location : {}", location);
        if (location.getId() == null) {
            return createLocation(location,principal);
        }
        ZonedDateTime lastmodifieddate = ZonedDateTime.now(ZoneId.systemDefault());
        User user = userRepository.findByLogin(principal.getName());
        location.setDomain(user.getDomain());
        location.setStatus(Status.Active);
        location.setLastmodifiedby(principal.getName());
        location.setLastmodifieddate(lastmodifieddate);
        Location result = locationRepository.save(location);
        locationSearchRepository.save(location);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("location", location.getId().toString()))
            .body(result);
    }

    /**
     * GET  /locations -> get all the locations.
     */
    @RequestMapping(value = "/locations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Location>> getAllLocations(Pageable pageable)
        throws URISyntaxException {
        Page<Location> page = locationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/locations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /location -> get all the location.
     */
    @RequestMapping(value = "/locations/{paginationOptions.pageNumber}/{paginationOptions.pageSize}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Location>> getAlllocations(HttpServletRequest request, Principal principal, @PathVariable("paginationOptions.pageNumber") String pageNumber,
            @PathVariable("paginationOptions.pageSize") String pageSize
    )
            throws URISyntaxException {
        int thepage = Integer.parseInt(pageNumber);
        int thepagesize = Integer.parseInt(pageSize);
        User user = userRepository.findByLogin(principal.getName());
        PageRequest pageRequest = new PageRequest(thepage, thepagesize, Sort.Direction.ASC, "id");
        Page<Location> data = locationRepository.findByDomain(user.getDomain(), pageRequest);
        return new ResponseEntity<>(data.getContent(), HttpStatus.OK);
    }
    
    /**
     * GET /location -> get all the location By Asset.
     */
    @RequestMapping(value = "/locationsByAsset/{id}/{paginationOptions.pageNumber}/{paginationOptions.pageSize}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Location>> getAlllocationsByAsset(HttpServletRequest request, Principal principal,@PathVariable("id") long id ,@PathVariable("paginationOptions.pageNumber") String pageNumber,
            @PathVariable("paginationOptions.pageSize") String pageSize
    )
            throws URISyntaxException {
        int thepage = Integer.parseInt(pageNumber);
        int thepagesize = Integer.parseInt(pageSize);
        User user = userRepository.findByLogin(principal.getName());
        PageRequest pageRequest = new PageRequest(thepage, thepagesize, Sort.Direction.ASC, "id");
        Page<Location> data = locationRepository.findByDomainAndAssetId(user.getDomain(), id,pageRequest);
        return new ResponseEntity<>(data.getContent(), HttpStatus.OK);
    }
    
    /**
     * GET /location/count -> Get Records Size
     */
    @RequestMapping(value = "/location/recordsLength",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public long getlength(Principal principal)
            throws URISyntaxException {
        User user = userRepository.findByLogin(principal.getName());
        long length = locationRepository.countByDomain(user.getDomain());
        return length;
    }
    
    /**
     * GET  /locations/:id -> get the "id" location.
     */
    @RequestMapping(value = "/locations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Location> getLocation(@PathVariable Long id) {
        log.debug("REST request to get Location : {}", id);
        return Optional.ofNullable(locationRepository.findOne(id))
            .map(location -> new ResponseEntity<>(
                location,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /locations/asset/:id -> get the "id" asset.
     */
    @RequestMapping(value = "/locations/asset/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Set<Location>> getLocationByAsset(@PathVariable Long id) {
        log.debug("REST request to get Location : {}", id);
        return Optional.ofNullable(locationRepository.findByAssetIds(id))
            .map(location -> new ResponseEntity<>(
                location,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * DELETE  /locations/:id -> delete the "id" location.
     */
    @RequestMapping(value = "/locations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        log.debug("REST request to delete Location : {}", id);
        locationRepository.delete(id);
        locationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("location", id.toString())).build();
    }

    /**
     * SEARCH  /_search/locations/:query -> search for the location corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/locations/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Location> searchLocations(@PathVariable String query) {
        return StreamSupport
            .stream(locationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
    /**
     * GET -> index location.
     */
    @RequestMapping(value = "indexLocation",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void add() {
        List<Location> locations = locationRepository.findAll();
        log.debug("In IndexResource.java");

        for (Location location:locations) {
            String id = location.getId().toString();
            IndexQuery indexQuery = new IndexQueryBuilder().withId(id).withObject(location).build();
            elasticsearchTemplate.index(indexQuery);
        }
    }
    
    /**
     * GET  /locationIsprimary/:assetId -> get location By Asset Id and isprimary.
     */
    @RequestMapping(value = "/locationIsprimary/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Location getByIsprimary(@PathVariable Long id) {
        log.debug("REST request to get Location By asset Id : {}", id);
        return locationRepository.findByAssetIdAndIsprimary(id,true);
    }
}
