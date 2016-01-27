package com.innvo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.innvo.domain.Eventmbr;
import com.innvo.repository.EventmbrRepository;
import com.innvo.repository.search.EventmbrSearchRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Eventmbr.
 */
@RestController
@RequestMapping("/api")
public class EventmbrResource {

    private final Logger log = LoggerFactory.getLogger(EventmbrResource.class);
        
    @Inject
    private EventmbrRepository eventmbrRepository;
    
    @Inject
    private EventmbrSearchRepository eventmbrSearchRepository;
    
    /**
     * POST  /eventmbrs -> Create a new eventmbr.
     */
    @RequestMapping(value = "/eventmbrs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Eventmbr> createEventmbr(@Valid @RequestBody Eventmbr eventmbr) throws URISyntaxException {
        log.debug("REST request to save Eventmbr : {}", eventmbr);
        if (eventmbr.getId() != null) {
             return ResponseEntity.badRequest().header("Failure", "A new eventmbr cannot already have an ID").body(null);
            //return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("eventmbr", "idexists", "A new eventmbr cannot already have an ID")).body(null);
        }
        Eventmbr result = eventmbrRepository.save(eventmbr);
        eventmbrSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/eventmbrs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("eventmbr", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /eventmbrs -> Updates an existing eventmbr.
     */
    @RequestMapping(value = "/eventmbrs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Eventmbr> updateEventmbr(@Valid @RequestBody Eventmbr eventmbr) throws URISyntaxException {
        log.debug("REST request to update Eventmbr : {}", eventmbr);
        if (eventmbr.getId() == null) {
            return createEventmbr(eventmbr);
        }
        Eventmbr result = eventmbrRepository.save(eventmbr);
        eventmbrSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("eventmbr", eventmbr.getId().toString()))
            .body(result);
    }

    /**
     * GET  /eventmbrs -> get all the eventmbrs.
     */
    @RequestMapping(value = "/eventmbrs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Eventmbr>> getAllEventmbrs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Eventmbrs");
        Page<Eventmbr> page = eventmbrRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/eventmbrs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /eventmbrs/:id -> get the "id" eventmbr.
     */
    @RequestMapping(value = "/eventmbrs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Eventmbr> getEventmbr(@PathVariable Long id) {
        log.debug("REST request to get Eventmbr : {}", id);
        Eventmbr eventmbr = eventmbrRepository.findOne(id);
        return Optional.ofNullable(eventmbr)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /eventmbrs/:id -> delete the "id" eventmbr.
     */
    @RequestMapping(value = "/eventmbrs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEventmbr(@PathVariable Long id) {
        log.debug("REST request to delete Eventmbr : {}", id);
        eventmbrRepository.delete(id);
        eventmbrSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("eventmbr", id.toString())).build();
    }

    /**
     * SEARCH  /_search/eventmbrs/:query -> search for the eventmbr corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/eventmbrs/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Eventmbr> searchEventmbrs(@PathVariable String query) {
        log.debug("REST request to search Eventmbrs for query {}", query);
        return StreamSupport
            .stream(eventmbrSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
