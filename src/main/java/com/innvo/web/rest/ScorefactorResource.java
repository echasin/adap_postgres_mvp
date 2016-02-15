package com.innvo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.innvo.domain.Scorefactor;
import com.innvo.repository.ScorefactorRepository;
import com.innvo.repository.search.ScorefactorSearchRepository;
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
 * REST controller for managing Scorefactor.
 */
@RestController
@RequestMapping("/api")
public class ScorefactorResource {

    private final Logger log = LoggerFactory.getLogger(ScorefactorResource.class);
        
    @Inject
    private ScorefactorRepository scorefactorRepository;
    
    @Inject
    private ScorefactorSearchRepository scorefactorSearchRepository;
    
    /**
     * POST  /scorefactors -> Create a new scorefactor.
     */
    @RequestMapping(value = "/scorefactors",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Scorefactor> createScorefactor(@Valid @RequestBody Scorefactor scorefactor) throws URISyntaxException {
        log.debug("REST request to save Scorefactor : {}", scorefactor);
        if (scorefactor.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new scorefactor cannot already have an ID").body(null);
        }
        
        
        
        Scorefactor result = scorefactorRepository.save(scorefactor);
        scorefactorSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/scorefactors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("scorefactor", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scorefactors -> Updates an existing scorefactor.
     */
    @RequestMapping(value = "/scorefactors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Scorefactor> updateScorefactor(@Valid @RequestBody Scorefactor scorefactor) throws URISyntaxException {
        log.debug("REST request to update Scorefactor : {}", scorefactor);
        if (scorefactor.getId() == null) {
            return createScorefactor(scorefactor);
        }
        Scorefactor result = scorefactorRepository.save(scorefactor);
        scorefactorSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("scorefactor", scorefactor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scorefactors -> get all the scorefactors.
     */
    @RequestMapping(value = "/scorefactors",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Scorefactor>> getAllScorefactors(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Scorefactors");
        Page<Scorefactor> page = scorefactorRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scorefactors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /scorefactors/:id -> get the "id" scorefactor.
     */
    @RequestMapping(value = "/scorefactors/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Scorefactor> getScorefactor(@PathVariable Long id) {
        log.debug("REST request to get Scorefactor : {}", id);
        Scorefactor scorefactor = scorefactorRepository.findOne(id);
        return Optional.ofNullable(scorefactor)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /scorefactors/:id -> delete the "id" scorefactor.
     */
    @RequestMapping(value = "/scorefactors/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteScorefactor(@PathVariable Long id) {
        log.debug("REST request to delete Scorefactor : {}", id);
        scorefactorRepository.delete(id);
        scorefactorSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("scorefactor", id.toString())).build();
    }

    /**
     * SEARCH  /_search/scorefactors/:query -> search for the scorefactor corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/scorefactors/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Scorefactor> searchScorefactors(@PathVariable String query) {
        log.debug("REST request to search Scorefactors for query {}", query);
        return StreamSupport
            .stream(scorefactorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
