package com.innvo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.innvo.domain.Segment;
import com.innvo.repository.SegmentRepository;
import com.innvo.repository.search.SegmentSearchRepository;
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
 * REST controller for managing Segment.
 */
@RestController
@RequestMapping("/api")
public class SegmentResource {

    private final Logger log = LoggerFactory.getLogger(SegmentResource.class);
        
    @Inject
    private SegmentRepository segmentRepository;
    
    @Inject
    private SegmentSearchRepository segmentSearchRepository;
    
    /**
     * POST  /segments -> Create a new segment.
     */
    @RequestMapping(value = "/segments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Segment> createSegment(@Valid @RequestBody Segment segment) throws URISyntaxException {
        log.debug("REST request to save Segment : {}", segment);
        if (segment.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new segment cannot already have an ID").body(null);
            // Commented Out echasin - Migrated code from adap_postgres_mvp_codegen_oauth.  Replace with code above.
            //return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("segment", "idexists", "A new segment cannot already have an ID")).body(null);
        }
        Segment result = segmentRepository.save(segment);
        segmentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/segments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("segment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /segments -> Updates an existing segment.
     */
    @RequestMapping(value = "/segments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Segment> updateSegment(@Valid @RequestBody Segment segment) throws URISyntaxException {
        log.debug("REST request to update Segment : {}", segment);
        if (segment.getId() == null) {
            return createSegment(segment);
        }
        Segment result = segmentRepository.save(segment);
        segmentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("segment", segment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /segments -> get all the segments.
     */
    @RequestMapping(value = "/segments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Segment>> getAllSegments(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Segments");
        Page<Segment> page = segmentRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/segments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /segments/:id -> get the "id" segment.
     */
    @RequestMapping(value = "/segments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Segment> getSegment(@PathVariable Long id) {
        log.debug("REST request to get Segment : {}", id);
        Segment segment = segmentRepository.findOne(id);
        return Optional.ofNullable(segment)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /segments/:id -> delete the "id" segment.
     */
    @RequestMapping(value = "/segments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSegment(@PathVariable Long id) {
        log.debug("REST request to delete Segment : {}", id);
        segmentRepository.delete(id);
        segmentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("segment", id.toString())).build();
    }

    /**
     * SEARCH  /_search/segments/:query -> search for the segment corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/segments/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Segment> searchSegments(@PathVariable String query) {
        log.debug("REST request to search Segments for query {}", query);
        return StreamSupport
            .stream(segmentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
