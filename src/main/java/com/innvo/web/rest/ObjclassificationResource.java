package com.innvo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.innvo.domain.Objclassification;
import com.innvo.domain.User;
import com.innvo.repository.ObjclassificationRepository;
import com.innvo.repository.UserRepository;
import com.innvo.repository.search.ObjclassificationSearchRepository;
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
 * REST controller for managing Objclassification.
 */
@RestController
@RequestMapping("/api")
public class ObjclassificationResource {

    private final Logger log = LoggerFactory.getLogger(ObjclassificationResource.class);

    @Inject
    private ObjclassificationRepository objclassificationRepository;

    @Inject
    private ObjclassificationSearchRepository objclassificationSearchRepository;

    @Inject
    UserRepository userRepository;
    /**
     * POST  /objclassifications -> Create a new objclassification.
     */
    @RequestMapping(value = "/objclassifications",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Objclassification> createObjclassification(@Valid @RequestBody Objclassification objclassification) throws URISyntaxException {
        log.debug("REST request to save Objclassification : {}", objclassification);
        if (objclassification.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new objclassification cannot already have an ID").body(null);
        }
        Objclassification result = objclassificationRepository.save(objclassification);
        objclassificationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/objclassifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("objclassification", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /objclassifications -> Updates an existing objclassification.
     */
    @RequestMapping(value = "/objclassifications",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Objclassification> updateObjclassification(@Valid @RequestBody Objclassification objclassification) throws URISyntaxException {
        log.debug("REST request to update Objclassification : {}", objclassification);
        if (objclassification.getId() == null) {
            return createObjclassification(objclassification);
        }
        Objclassification result = objclassificationRepository.save(objclassification);
        objclassificationSearchRepository.save(objclassification);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("objclassification", objclassification.getId().toString()))
            .body(result);
    }

    /**
     * GET  /objclassifications -> get all the objclassifications.
     */
    @RequestMapping(value = "/objclassifications",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Objclassification>> getAllObjclassifications(Pageable pageable)
        throws URISyntaxException {
        Page<Objclassification> page = objclassificationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/objclassifications");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /objclassifications/:id -> get the "id" objclassification.
     */
    @RequestMapping(value = "/objclassifications/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Objclassification> getObjclassification(@PathVariable Long id) {
        log.debug("REST request to get Objclassification : {}", id);
        return Optional.ofNullable(objclassificationRepository.findOne(id))
            .map(objclassification -> new ResponseEntity<>(
                objclassification,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET /obj_classifications -> get all the Classifications by RecordtypeId.
     */
    @RequestMapping(value = "/classifications/recordtype/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Objclassification>> getAllByRecordtype(
            @PathVariable Long id,Principal principal)
          throws URISyntaxException {
        User user = userRepository.findByLogin(principal.getName());
        List<Objclassification> list = objclassificationRepository.findByObjrecordtypeIdAndDomain(id, user.getDomain());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    
    /**
     * DELETE  /objclassifications/:id -> delete the "id" objclassification.
     */
    @RequestMapping(value = "/objclassifications/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteObjclassification(@PathVariable Long id) {
        log.debug("REST request to delete Objclassification : {}", id);
        objclassificationRepository.delete(id);
        objclassificationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("objclassification", id.toString())).build();
    }

    /**
     * SEARCH  /_search/objclassifications/:query -> search for the objclassification corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/objclassifications/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Objclassification> searchObjclassifications(@PathVariable String query) {
        return StreamSupport
            .stream(objclassificationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
