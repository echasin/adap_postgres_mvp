package com.innvo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.innvo.domain.Objtype;
import com.innvo.domain.User;
import com.innvo.repository.ObjtypeRepository;
import com.innvo.repository.UserRepository;
import com.innvo.repository.search.ObjtypeSearchRepository;
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
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Objtype.
 */
@RestController
@RequestMapping("/api")
public class ObjtypeResource {

    private final Logger log = LoggerFactory.getLogger(ObjtypeResource.class);

    @Inject
    private ObjtypeRepository objtypeRepository;

    @Inject
    private ObjtypeSearchRepository objtypeSearchRepository;

    @Inject
    UserRepository userRepository;
    /**
     * POST  /objtypes -> Create a new objtype.
     */
    @RequestMapping(value = "/objtypes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Objtype> createObjtype(@Valid @RequestBody Objtype objtype) throws URISyntaxException {
        log.debug("REST request to save Objtype : {}", objtype);
        if (objtype.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new objtype cannot already have an ID").body(null);
        }
        Objtype result = objtypeRepository.save(objtype);
        objtypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/objtypes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("objtype", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /objtypes -> Updates an existing objtype.
     */
    @RequestMapping(value = "/objtypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Objtype> updateObjtype(@Valid @RequestBody Objtype objtype) throws URISyntaxException {
        log.debug("REST request to update Objtype : {}", objtype);
        if (objtype.getId() == null) {
            return createObjtype(objtype);
        }
        Objtype result = objtypeRepository.save(objtype);
        objtypeSearchRepository.save(objtype);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("objtype", objtype.getId().toString()))
            .body(result);
    }

    /**
     * GET  /objtypes -> get all the objtypes.
     */
    @RequestMapping(value = "/objtypes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Objtype>> getAllObjtypes(Pageable pageable)
        throws URISyntaxException {
        Page<Objtype> page = objtypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/objtypes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /objtypes/:id -> get the "id" objtype.
     */
    @RequestMapping(value = "/objtypes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Objtype> getObjtype(@PathVariable Long id) {
        log.debug("REST request to get Objtype : {}", id);
        return Optional.ofNullable(objtypeRepository.findOne(id))
            .map(objtype -> new ResponseEntity<>(
                objtype,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET /obj_types -> get all the Types by CategoryId.
     */
    @RequestMapping(value = "/types/category/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Objtype>> getAllByCategoryId(
            @PathVariable Long id,Principal principal)
          throws URISyntaxException {
        User user = userRepository.findByLogin(principal.getName());
        List<Objtype> list = objtypeRepository.findByObjcategoryIdAndDomain(id, user.getDomain());
        return new ResponseEntity<>(list ,HttpStatus.OK);
    }
    
    /**
     * DELETE  /objtypes/:id -> delete the "id" objtype.
     */
    @RequestMapping(value = "/objtypes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteObjtype(@PathVariable Long id) {
        log.debug("REST request to delete Objtype : {}", id);
        objtypeRepository.delete(id);
        objtypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("objtype", id.toString())).build();
    }

    /**
     * SEARCH  /_search/objtypes/:query -> search for the objtype corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/objtypes/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Objtype> searchObjtypes(@PathVariable String query) {
        return StreamSupport
            .stream(objtypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
