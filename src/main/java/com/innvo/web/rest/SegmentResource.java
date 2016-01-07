package com.innvo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.innvo.domain.Segment;
import com.innvo.domain.User;
import com.innvo.domain.enumeration.Status;
import com.innvo.repository.SegmentRepository;
import com.innvo.repository.UserRepository;
import com.innvo.repository.search.SegmentSearchRepository;
import com.innvo.web.rest.util.HeaderUtil;
import com.innvo.web.rest.util.PaginationUtil;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
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
    
    @Inject
    UserRepository userRepository;
    
    @Inject
    ElasticsearchTemplate elasticsearchTemplate;
    /**
     * POST  /segments -> Create a new segment.
     */
    @RequestMapping(value = "/segments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Segment> createSegment(@Valid @RequestBody Segment segment,Principal principal) throws URISyntaxException {
        log.debug("REST request to save Segment : {}", segment);
        if (segment.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new segment cannot already have an ID").body(null);
            // Commented Out echasin - Migrated code from adap_postgres_mvp_codegen_oauth.  Replace with code above.
            //return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("segment", "idexists", "A new segment cannot already have an ID")).body(null);
        }
        ZonedDateTime lastmodifieddate = ZonedDateTime.now(ZoneId.systemDefault());
        User user = userRepository.findByLogin(principal.getName());
        segment.setDomain(user.getDomain());
        segment.setStatus(Status.Active);
        segment.setLastmodifiedby(principal.getName());
        segment.setLastmodifieddate(lastmodifieddate);
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
    public ResponseEntity<Segment> updateSegment(@Valid @RequestBody Segment segment,Principal principal) throws URISyntaxException {
        log.debug("REST request to update Segment : {}", segment);
        if (segment.getId() == null) {
            return createSegment(segment,principal);
        }
        ZonedDateTime lastmodifieddate = ZonedDateTime.now(ZoneId.systemDefault());
        User user = userRepository.findByLogin(principal.getName());
        segment.setDomain(user.getDomain());
        segment.setStatus(Status.Active);
        segment.setLastmodifiedby(principal.getName());
        segment.setLastmodifieddate(lastmodifieddate);
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
     * GET /segments -> get all the segments.
     */
    @RequestMapping(value = "/segments/{paginationOptions.pageNumber}/{paginationOptions.pageSize}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Segment>> getAllSegments(HttpServletRequest request, Principal principal, @PathVariable("paginationOptions.pageNumber") String pageNumber,
            @PathVariable("paginationOptions.pageSize") String pageSize
    )
            throws URISyntaxException {
        int thepage = Integer.parseInt(pageNumber);
        int thepagesize = Integer.parseInt(pageSize);
        User user = userRepository.findByLogin(principal.getName());
        PageRequest pageRequest = new PageRequest(thepage, thepagesize, Sort.Direction.ASC, "id");
        Page<Segment> data = segmentRepository.findAll(pageRequest);
  		System.out.println("55555555555555555555");
  		System.out.println(data);
  		System.out.println("666666666666666666666666666");
        return new ResponseEntity<>(data.getContent(), HttpStatus.OK);
    }

    /**
     * GET /segment/count -> Get Records Size
     */
    @RequestMapping(value = "/segments/recordsLength",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public long getlength(Principal principal)
            throws URISyntaxException {
        User user = userRepository.findByLogin(principal.getName());
        long length = segmentRepository.countByDomain(user.getDomain());
        return length;
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
    public List<Segment> searchSegments(@PathVariable String query,Principal principal) {
        log.debug("REST request to search Segments for query {}", query);
        User user = userRepository.findByLogin(principal.getName());
    	QueryBuilder filterByDomain = termQuery("domain", user.getDomain()); 
    	QueryBuilder queryBuilder = queryStringQuery(query); 
    	BoolQueryBuilder bool = new BoolQueryBuilder()
    			.must(queryBuilder)
                .must(filterByDomain);

      	List<Segment> result = Lists.newArrayList(segmentSearchRepository.search(bool));
		return result;
    }
    
    /**
     * GET -> index segments.
     */
    @RequestMapping(value = "indexsegment",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void add() {
        List<Segment> segments = segmentRepository.findAll();
        log.debug("In IndexResource.java");

        for (Segment segment:segments) {
            String id = segment.getId().toString();
            IndexQuery indexQuery = new IndexQueryBuilder().withId(id).withObject(segment).build();
            elasticsearchTemplate.index(indexQuery);
        }
    }
}
