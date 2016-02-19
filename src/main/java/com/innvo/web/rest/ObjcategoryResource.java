package com.innvo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.innvo.domain.Objcategory;
import com.innvo.domain.User;
import com.innvo.repository.ObjcategoryRepository;
import com.innvo.repository.UserRepository;
import com.innvo.repository.search.ObjcategorySearchRepository;
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
 * REST controller for managing Objcategory.
 */
@RestController
@RequestMapping("/api")
public class ObjcategoryResource {

    private final Logger log = LoggerFactory.getLogger(ObjcategoryResource.class);

    @Inject
    private ObjcategoryRepository objcategoryRepository;

    @Inject
    private ObjcategorySearchRepository objcategorySearchRepository;

    @Inject
    UserRepository userRepository;
    /**
     * POST  /objcategorys -> Create a new objcategory.
     */
    @RequestMapping(value = "/objcategorys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Objcategory> createObjcategory(@Valid @RequestBody Objcategory objcategory) throws URISyntaxException {
        log.debug("REST request to save Objcategory : {}", objcategory);
        if (objcategory.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new objcategory cannot already have an ID").body(null);
        }
        Objcategory result = objcategoryRepository.save(objcategory);
        objcategorySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/objcategorys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("objcategory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /objcategorys -> Updates an existing objcategory.
     */
    @RequestMapping(value = "/objcategorys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Objcategory> updateObjcategory(@Valid @RequestBody Objcategory objcategory) throws URISyntaxException {
        log.debug("REST request to update Objcategory : {}", objcategory);
        if (objcategory.getId() == null) {
            return createObjcategory(objcategory);
        }
        Objcategory result = objcategoryRepository.save(objcategory);
        objcategorySearchRepository.save(objcategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("objcategory", objcategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /objcategorys -> get all the objcategorys.
     */
    @RequestMapping(value = "/objcategorys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Objcategory>> getAllObjcategorys(Pageable pageable)
        throws URISyntaxException {
        Page<Objcategory> page = objcategoryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/objcategorys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /objcategorys/:id -> get the "id" objcategory.
     */
    @RequestMapping(value = "/objcategorys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Objcategory> getObjcategory(@PathVariable Long id) {
        log.debug("REST request to get Objcategory : {}", id);
        return Optional.ofNullable(objcategoryRepository.findOne(id))
            .map(objcategory -> new ResponseEntity<>(
                objcategory,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET /obj_classifications -> get all the catergory by classification id.
     */
    @RequestMapping(value = "/categorys/classification/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Objcategory>> getAllByClassificationId(
            @PathVariable Long id, Principal principal)
            throws URISyntaxException {
        User user = userRepository.findByLogin(principal.getName());
        List<Objcategory> list = objcategoryRepository.findByObjclassificationIdAndDomain(id, user.getDomain());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    
    /**
     * DELETE  /objcategorys/:id -> delete the "id" objcategory.
     */
    @RequestMapping(value = "/objcategorys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteObjcategory(@PathVariable Long id) {
        log.debug("REST request to delete Objcategory : {}", id);
        objcategoryRepository.delete(id);
        objcategorySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("objcategory", id.toString())).build();
    }

    /**
     * SEARCH  /_search/objcategorys/:query -> search for the objcategory corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/objcategorys/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Objcategory> searchObjcategorys(@PathVariable String query) {
        return StreamSupport
            .stream(objcategorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
