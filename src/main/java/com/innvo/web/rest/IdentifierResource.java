package com.innvo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.innvo.domain.Identifier;
import com.innvo.repository.IdentifierRepository;
import com.innvo.repository.search.IdentifierSearchRepository;
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
 * REST controller for managing Identifier.
 */
@RestController
@RequestMapping("/api")
public class IdentifierResource {

    private final Logger log = LoggerFactory.getLogger(IdentifierResource.class);
        
    @Inject
    private IdentifierRepository identifierRepository;
    
    @Inject
    private IdentifierSearchRepository identifierSearchRepository;
    
    /**
     * POST  /identifiers -> Create a new identifier.
     */
    @RequestMapping(value = "/identifiers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Identifier> createIdentifier(@Valid @RequestBody Identifier identifier) throws URISyntaxException {
        log.debug("REST request to save Identifier : {}", identifier);
        if (identifier.getId() != null) {
             return ResponseEntity.badRequest().header("Failure", "A new identifier cannot already have an ID").body(null);
            // Commented Out echasin - Migrated code from adap_postgres_mvp_codegen_oauth.  Replace with code above.
            //return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("identifier", "idexists", "A new identifier cannot already have an ID")).body(null);
        }
        Identifier result = identifierRepository.save(identifier);
        identifierSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/identifiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("identifier", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /identifiers -> Updates an existing identifier.
     */
    @RequestMapping(value = "/identifiers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Identifier> updateIdentifier(@Valid @RequestBody Identifier identifier) throws URISyntaxException {
        log.debug("REST request to update Identifier : {}", identifier);
        if (identifier.getId() == null) {
            return createIdentifier(identifier);
        }
        Identifier result = identifierRepository.save(identifier);
        identifierSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("identifier", identifier.getId().toString()))
            .body(result);
    }

    /**
     * GET  /identifiers -> get all the identifiers.
     */
    @RequestMapping(value = "/identifiers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Identifier>> getAllIdentifiers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Identifiers");
        Page<Identifier> page = identifierRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/identifiers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /identifiers/:id -> get the "id" identifier.
     */
    @RequestMapping(value = "/identifiers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Identifier> getIdentifier(@PathVariable Long id) {
        log.debug("REST request to get Identifier : {}", id);
        Identifier identifier = identifierRepository.findOne(id);
        return Optional.ofNullable(identifier)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /identifiers/:id -> delete the "id" identifier.
     */
    @RequestMapping(value = "/identifiers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteIdentifier(@PathVariable Long id) {
        log.debug("REST request to delete Identifier : {}", id);
        identifierRepository.delete(id);
        identifierSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("identifier", id.toString())).build();
    }

    /**
     * SEARCH  /_search/identifiers/:query -> search for the identifier corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/identifiers/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Identifier> searchIdentifiers(@PathVariable String query) {
        log.debug("REST request to search Identifiers for query {}", query);
        return StreamSupport
            .stream(identifierSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
