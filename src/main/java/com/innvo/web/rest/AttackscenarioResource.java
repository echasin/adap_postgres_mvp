package com.innvo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.innvo.domain.Attackscenario;
import com.innvo.repository.AttackscenarioRepository;
import com.innvo.repository.search.AttackscenarioSearchRepository;
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
 * REST controller for managing Attackscenario.
 */
@RestController
@RequestMapping("/api")
public class AttackscenarioResource {

    private final Logger log = LoggerFactory.getLogger(AttackscenarioResource.class);
        
    @Inject
    private AttackscenarioRepository attackscenarioRepository;
    
    @Inject
    private AttackscenarioSearchRepository attackscenarioSearchRepository;
    
    /**
     * POST  /attackscenarios -> Create a new attackscenario.
     */
    @RequestMapping(value = "/attackscenarios",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Attackscenario> createAttackscenario(@Valid @RequestBody Attackscenario attackscenario) throws URISyntaxException {
        log.debug("REST request to save Attackscenario : {}", attackscenario);
        if (attackscenario.getId() != null) {
             return ResponseEntity.badRequest().header("Failure", "A new attackscenerio cannot already have an ID").body(null);
           // Commented Out echasin - Migrated code from adap_postgres_mvp_codegen_oauth.  Replace with code above.
           //return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("attackscenario", "idexists", "A new attackscenario cannot already have an ID")).body(null);
        }
        Attackscenario result = attackscenarioRepository.save(attackscenario);
        attackscenarioSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/attackscenarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("attackscenario", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /attackscenarios -> Updates an existing attackscenario.
     */
    @RequestMapping(value = "/attackscenarios",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Attackscenario> updateAttackscenario(@Valid @RequestBody Attackscenario attackscenario) throws URISyntaxException {
        log.debug("REST request to update Attackscenario : {}", attackscenario);
        if (attackscenario.getId() == null) {
            return createAttackscenario(attackscenario);
        }
        Attackscenario result = attackscenarioRepository.save(attackscenario);
        attackscenarioSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("attackscenario", attackscenario.getId().toString()))
            .body(result);
    }

    /**
     * GET  /attackscenarios -> get all the attackscenarios.
     */
    @RequestMapping(value = "/attackscenarios",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Attackscenario>> getAllAttackscenarios(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Attackscenarios");
        Page<Attackscenario> page = attackscenarioRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/attackscenarios");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /attackscenarios/:id -> get the "id" attackscenario.
     */
    @RequestMapping(value = "/attackscenarios/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Attackscenario> getAttackscenario(@PathVariable Long id) {
        log.debug("REST request to get Attackscenario : {}", id);
        Attackscenario attackscenario = attackscenarioRepository.findOne(id);
        return Optional.ofNullable(attackscenario)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /attackscenarios/:id -> delete the "id" attackscenario.
     */
    @RequestMapping(value = "/attackscenarios/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAttackscenario(@PathVariable Long id) {
        log.debug("REST request to delete Attackscenario : {}", id);
        attackscenarioRepository.delete(id);
        attackscenarioSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("attackscenario", id.toString())).build();
    }

    /**
     * SEARCH  /_search/attackscenarios/:query -> search for the attackscenario corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/attackscenarios/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Attackscenario> searchAttackscenarios(@PathVariable String query) {
        log.debug("REST request to search Attackscenarios for query {}", query);
        return StreamSupport
            .stream(attackscenarioSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
