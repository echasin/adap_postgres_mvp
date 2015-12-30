package com.innvo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.innvo.domain.Asset;
import com.innvo.domain.Filter;
import com.innvo.repository.FilterRepository;
import com.innvo.repository.search.FilterSearchRepository;
import com.innvo.web.rest.util.HeaderUtil;
import com.innvo.web.rest.util.PaginationUtil;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.builder.SearchSourceBuilder;

//import scala.util.parsing.json.JSON;
import static org.elasticsearch.node.NodeBuilder.*;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.*;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.IndicesQueryBuilder;
import org.elasticsearch.index.query.ParsedQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WrapperQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Filter.
 */
@RestController
@RequestMapping("/api")
public class FilterResource {

    private final Logger log = LoggerFactory.getLogger(FilterResource.class);

    @Inject
    private FilterRepository filterRepository;

    @Inject
    private FilterSearchRepository filterSearchRepository;

    @Inject
    ElasticsearchTemplate elasticsearchTemplate;
    /**
     * POST  /filters -> Create a new filter.
     */
    @RequestMapping(value = "/filters",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Filter> createFilter(@Valid @RequestBody Filter filter) throws URISyntaxException {
        log.debug("REST request to save Filter : {}", filter);
        if (filter.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new filter cannot already have an ID").body(null);
        }
        Filter result = filterRepository.save(filter);
        filterSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/filters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("filter", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /filters -> Updates an existing filter.
     */
    @RequestMapping(value = "/filters",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Filter> updateFilter(@Valid @RequestBody Filter filter) throws URISyntaxException {
        log.debug("REST request to update Filter : {}", filter);
        if (filter.getId() == null) {
            return createFilter(filter);
        }
        Filter result = filterRepository.save(filter);
        filterSearchRepository.save(filter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("filter", filter.getId().toString()))
            .body(result);
    }

    /**
     * GET  /filters -> get all the filters.
     */
    @RequestMapping(value = "/filters",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Filter>> getAllFilters(Pageable pageable)
        throws URISyntaxException {
        Page<Filter> page = filterRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/filters");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /filters -> get  the filters By Recordtype.
     */
    @RequestMapping(value = "/filtersByRecordtype",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Filter>> geFiltersByRecordtype()
        throws URISyntaxException {
        List<Filter> list = filterRepository.findByObjrecordtypeName("Asset");
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * GET  /filters/:id -> get the "id" filter.
     */
    @RequestMapping(value = "/filters/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Filter> getFilter(@PathVariable Long id) {
        log.debug("REST request to get Filter : {}", id);
        return Optional.ofNullable(filterRepository.findOne(id))
            .map(filter -> new ResponseEntity<>(
                filter,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /filters/:id -> delete the "id" filter.
     */
    @RequestMapping(value = "/filters/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFilter(@PathVariable Long id) {
        log.debug("REST request to delete Filter : {}", id);
        filterRepository.delete(id);
        filterSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("filter", id.toString())).build();
    }

    /**
     * SEARCH  /_search/filters/:query -> search for the filter corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/filters/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Filter> searchFilters(@PathVariable String query) {
	SearchQuery searchQuery = new 
	         NativeSearchQueryBuilder()
    		    .withQuery(queryString(query))
    		    .withFilter(FilterBuilders.termFilter("domain","demo"))
    		    .build();
    
	 Page<Filter> filter =
    		    elasticsearchTemplate.queryForPage(searchQuery,Filter.class);
    		System.out.println(filter.getContent());    		
    
    		return filter.getContent();
    }
    

    /**
     * GET -> save filter by query builder.
     */
    @RequestMapping(value = "saveES/{rulejson}/{esjson:.+}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void elastic(HttpServletRequest request,
    		@PathVariable("rulejson") String rulejson,
    		@PathVariable("esjson") String esjson) {
 
    	long id=Long.parseLong(request.getParameter("id"));
    	BoolQueryBuilder bool = new BoolQueryBuilder();
        bool.must(new WrapperQueryBuilder(esjson));
      
        SearchQuery searchQuery = new 
        		 NativeSearchQueryBuilder()
    			.withQuery(bool)
    			.build();
    	System.out.println(searchQuery.getQuery());
    	Filter filter=filterRepository.findOne(id);
    	filter.setId(id);
    	//save query builder json format in queryspringdata field
    	filter.setQueryspringdata(rulejson);
    	filter.setQueryelastic(esjson);
    	filterRepository.save(filter);
    	/**
    	Page<Filter> filter =
    		    elasticsearchTemplate.queryForPage(searchQuery,Filter.class);
    		System.out.println(filter.getContent());    		
    	
    		return filter.getContent();
        **/
    }
    
    /**
     * GET -> index objects.
     */
    @RequestMapping(value = "indexfilter",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void add() {
        List<Filter> filters = filterRepository.findAll();
        log.debug("In IndexResource.java");

        for (Filter filter:filters) {
            String id = filter.getId().toString();
            IndexQuery indexQuery = new IndexQueryBuilder().withId(id).withObject(filter).build();
            elasticsearchTemplate.index(indexQuery);
        }
    }
}
