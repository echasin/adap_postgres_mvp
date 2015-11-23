package com.innvo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.innvo.domain.Objrecordtype;
import com.innvo.repository.ObjrecordtypeRepository;
import com.innvo.repository.search.ObjrecordtypeSearchRepository;
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
 * REST controller for managing Objrecordtype.
 */
@RestController
@RequestMapping("/api")
public class ObjrecordtypeResource {

    private final Logger log = LoggerFactory.getLogger(ObjrecordtypeResource.class);

    @Inject
    private ObjrecordtypeRepository objrecordtypeRepository;

    @Inject
    private ObjrecordtypeSearchRepository objrecordtypeSearchRepository;

    /**
     * POST  /objrecordtypes -> Create a new objrecordtype.
     */
    @RequestMapping(value = "/objrecordtypes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Objrecordtype> createObjrecordtype(@Valid @RequestBody Objrecordtype objrecordtype) throws URISyntaxException {
        log.debug("REST request to save Objrecordtype : {}", objrecordtype);
        if (objrecordtype.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new objrecordtype cannot already have an ID").body(null);
        }
        Objrecordtype result = objrecordtypeRepository.save(objrecordtype);
        objrecordtypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/objrecordtypes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("objrecordtype", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /objrecordtypes -> Updates an existing objrecordtype.
     */
    @RequestMapping(value = "/objrecordtypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Objrecordtype> updateObjrecordtype(@Valid @RequestBody Objrecordtype objrecordtype) throws URISyntaxException {
        log.debug("REST request to update Objrecordtype : {}", objrecordtype);
        if (objrecordtype.getId() == null) {
            return createObjrecordtype(objrecordtype);
        }
        Objrecordtype result = objrecordtypeRepository.save(objrecordtype);
        objrecordtypeSearchRepository.save(objrecordtype);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("objrecordtype", objrecordtype.getId().toString()))
            .body(result);
    }

    /**
     * GET  /objrecordtypes -> get all the objrecordtypes.
     */
    @RequestMapping(value = "/objrecordtypes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Objrecordtype>> getAllObjrecordtypes(Pageable pageable)
        throws URISyntaxException {
        Page<Objrecordtype> page = objrecordtypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/objrecordtypes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /objrecordtypes/:id -> get the "id" objrecordtype.
     */
    @RequestMapping(value = "/objrecordtypes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Objrecordtype> getObjrecordtype(@PathVariable Long id) {
        log.debug("REST request to get Objrecordtype : {}", id);
        return Optional.ofNullable(objrecordtypeRepository.findOne(id))
            .map(objrecordtype -> new ResponseEntity<>(
                objrecordtype,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /objrecordtypes/:id -> delete the "id" objrecordtype.
     */
    @RequestMapping(value = "/objrecordtypes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteObjrecordtype(@PathVariable Long id) {
        log.debug("REST request to delete Objrecordtype : {}", id);
        objrecordtypeRepository.delete(id);
        objrecordtypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("objrecordtype", id.toString())).build();
    }

    /**
     * SEARCH  /_search/objrecordtypes/:query -> search for the objrecordtype corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/objrecordtypes/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Objrecordtype> searchObjrecordtypes(@PathVariable String query) {
        return StreamSupport
            .stream(objrecordtypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
