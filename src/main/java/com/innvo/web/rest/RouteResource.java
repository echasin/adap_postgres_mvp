package com.innvo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.innvo.domain.Filter;
import com.innvo.domain.Location;
import com.innvo.domain.Route;
import com.innvo.domain.Score;
import com.innvo.domain.Segment;
import com.innvo.domain.User;
import com.innvo.domain.enumeration.Status;
import com.innvo.repository.FilterRepository;
import com.innvo.repository.LocationRepository;
import com.innvo.repository.RouteRepository;
import com.innvo.repository.ScoreRepository;
import com.innvo.repository.SegmentRepository;
import com.innvo.repository.UserRepository;
import com.innvo.repository.search.LocationSearchRepository;
import com.innvo.repository.search.RouteSearchRepository;
import com.innvo.repository.search.SegmentSearchRepository;
import com.innvo.web.rest.util.HeaderUtil;
import com.innvo.web.rest.util.PaginationUtil;
import com.innvo.web.rest.util.RouteUtil;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.WrapperQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AopInvocationException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Route.
 */
@RestController
@RequestMapping("/api")
public class RouteResource {

    private final Logger log = LoggerFactory.getLogger(RouteResource.class);
        
    @Inject
    private RouteRepository routeRepository;
    
    @Inject
    private RouteSearchRepository routeSearchRepository;
    
    @Inject
    private SegmentRepository segmentRepository;
    
    @Inject
    LocationRepository locationRepository;
    
    @Inject
    private SegmentSearchRepository segmentSearchRepository;
    
    @Inject
    LocationSearchRepository locationSearchRepository;
    
    @Inject
    FilterRepository filterRepository;
    
    @Inject
    ScoreRepository scoreRepository;
    
    @Inject
    UserRepository userRepository;
    
    
    @Inject
    ElasticsearchTemplate elasticsearchTemplate;
    /**
     * POST  /routes -> Create a new route.
     */
    @RequestMapping(value = "/routes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Route> createRoute(@Valid @RequestBody Route route,Principal principal) throws URISyntaxException {
        log.debug("REST request to save Route : {}", route);
        if (route.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new route cannot already have an ID").body(null);
            // Commented Out echasin - Migrated code from adap_postgres_mvp_codegen_oauth.  Replace with code above.
            //return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("route", "idexists", "A new route cannot already have an ID")).body(null);
        }
        ZonedDateTime lastmodifieddate = ZonedDateTime.now(ZoneId.systemDefault());
        User user = userRepository.findByLogin(principal.getName());
        route.setDomain(user.getDomain());
        route.setStatus(Status.Active);
        route.setLastmodifiedby(principal.getName());
        route.setLastmodifieddate(lastmodifieddate);
        Route result = routeRepository.save(route);
        routeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/routes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("route", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /routes -> Updates an existing route.
     */
    @RequestMapping(value = "/routes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Route> updateRoute(@Valid @RequestBody Route route,Principal principal) throws URISyntaxException {
        log.debug("REST request to update Route : {}", route);
        if (route.getId() == null) {
            return createRoute(route,principal);
        }
        ZonedDateTime lastmodifieddate = ZonedDateTime.now(ZoneId.systemDefault());
        User user = userRepository.findByLogin(principal.getName());
        route.setDomain(user.getDomain());
        route.setStatus(Status.Active);
        route.setLastmodifiedby(principal.getName());
        route.setLastmodifieddate(lastmodifieddate);
        Route result = routeRepository.save(route);
        routeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("route", route.getId().toString()))
            .body(result);
    }

    /**
     * GET  /routes -> get all the routes.
     */
    @RequestMapping(value = "/routes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Route>> getAllRoutes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Routes");
        Page<Route> page = routeRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/routes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /routes -> get all the routes.
     */
    @RequestMapping(value = "/routes/{paginationOptions.pageNumber}/{paginationOptions.pageSize}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RouteUtil>> getAllRoutes(HttpServletRequest request, Principal principal, @PathVariable("paginationOptions.pageNumber") String pageNumber,
            @PathVariable("paginationOptions.pageSize") String pageSize
    )
            throws URISyntaxException,AopInvocationException {
      
    	int thepage = Integer.parseInt(pageNumber);
        int thepagesize = Integer.parseInt(pageSize);
    	List<RouteUtil> list=new ArrayList<RouteUtil>();
        User user = userRepository.findByLogin(principal.getName());
        PageRequest pageRequest = new PageRequest(thepage, thepagesize, Sort.Direction.ASC, "id");
        Page<Route> data =routeRepository.findByDomain(user.getDomain(), pageRequest);
        
        long minSegmentNumber;
        long maxSegmentNumber;
        Segment firstSegment=null;
        Segment lastSegment=null;
     
        for(Route route:data.getContent()){
        	 RouteUtil routeUtil=new RouteUtil(); 
        	 
        	 try{      	 
        			 minSegmentNumber=segmentRepository.getMinSegmentnumberByRouteId(route.getId());
                	 maxSegmentNumber=segmentRepository.getMaxSegmentnumberByRouteId(route.getId());
                	 firstSegment=segmentRepository.findByRouteIdAndSegmentnumber(route.getId(), minSegmentNumber);
                	 lastSegment=segmentRepository.findByRouteIdAndSegmentnumber(route.getId(), maxSegmentNumber);
                    
                	 List<Segment> segments=segmentRepository.findByRouteId(route.getId());
                     List<Location> originLocations=new ArrayList<Location>();
                     List<Location> destinationLocations=new ArrayList<Location>();
                     List<String> originNames=new ArrayList<String>();
                     List<String> destinationNames=new ArrayList<String>();
                     
                     for(Segment segment:segments){
                     Location getOriginLocations=locationRepository.findByAssetId(segment.getAssetorigin().getId());
                     Location getDestinationLocations=locationRepository.findByAssetId(segment.getAssetdestination().getId());
                     segment.getAssetdestination().getName();
                     originNames.add(segment.getAssetorigin().getName());
                     destinationNames.add(segment.getAssetdestination().getName());
                     routeUtil.setOriginNames(originNames);routeUtil.setDestinationNames(destinationNames);
                    
                     originLocations.add(getOriginLocations);
                     destinationLocations.add(getDestinationLocations); 
                     routeUtil.setOriginLocations(originLocations);
                     routeUtil.setDestinationLocations(destinationLocations);
                     } 
                     
                     double sum=0;
                     double averageScore;
                     ZonedDateTime lastmodifieddate=scoreRepository.findMaxLastmodifieddateByRouteId(route.getId());
                     
                     if(lastmodifieddate==null){
                    	 averageScore=Double.NaN;
                    	 }	
                     else {
                    	 long runid=scoreRepository.findMaxRunid(lastmodifieddate,route.getId());
                         List<Score> scores=scoreRepository.findByRunidAndRouteId(runid, route.getId());
                         for(Score score:scores){
                        	 sum=sum+score.getValue();
                        	 }
                         averageScore=sum/scores.size();
                         }
                     
                     routeUtil.setRouteId(route.getId());
                     routeUtil.setRoutName(route.getName());
                     routeUtil.setOriginName(firstSegment.getAssetorigin().getName());
                     routeUtil.setDestinationName(lastSegment.getAssetdestination().getName());
                     routeUtil.setAverageScore(averageScore);
                     list.add(routeUtil);
        	 }catch(AopInvocationException e){
        		 routeUtil.setRouteId(route.getId());
        		 routeUtil.setAverageScore(Double.NaN);
        		 list.add(routeUtil);
        	 }
        }
   
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * GET /route/count -> Get Records Size
     */
    @RequestMapping(value = "/route/recordsLength",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public long getlength(Principal principal)
            throws URISyntaxException {
        User user = userRepository.findByLogin(principal.getName());
        long length = routeRepository.countByDomain(user.getDomain());
        return length;
    }
    /**
     * GET  /routes/:id -> get the "id" route.
     */
    @RequestMapping(value = "/routes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Route> getRoute(@PathVariable Long id) {
        log.debug("REST request to get Route : {}", id);
        Route route = routeRepository.findOne(id);
        return Optional.ofNullable(route)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /routes/:id -> delete the "id" route.
     */
    @RequestMapping(value = "/routes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        log.debug("REST request to delete Route : {}", id);
        routeRepository.delete(id);
        routeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("route", id.toString())).build();
    }

    /**
     * SEARCH  /_search/routes/:query -> search for the route corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/routes/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<RouteUtil> searchRoutes(@PathVariable String query,Principal principal) {
        log.debug("REST request to search Routes for query {}", query);
    	List<RouteUtil> list=new ArrayList<RouteUtil>();
        User user = userRepository.findByLogin(principal.getName());
    	QueryBuilder filterByDomain = termQuery("domain", user.getDomain()); 
    	QueryBuilder queryBuilder = queryStringQuery(query); 
    	BoolQueryBuilder bool = new BoolQueryBuilder()
    			.must(queryBuilder)
                .must(filterByDomain);

      	List<Route> result = Lists.newArrayList(routeSearchRepository.search(bool));
      	 long minSegmentNumber;
         long maxSegmentNumber;
         Segment firstSegment=null;
         Segment lastSegment=null;
      
         for(Route route:result){
         	 RouteUtil routeUtil=new RouteUtil();
           
         	 minSegmentNumber=segmentRepository.getMinSegmentnumberByRouteId(route.getId());
         	 maxSegmentNumber=segmentRepository.getMaxSegmentnumberByRouteId(route.getId());
         	 firstSegment=segmentRepository.findByRouteIdAndSegmentnumber(route.getId(), minSegmentNumber);
         	 lastSegment=segmentRepository.findByRouteIdAndSegmentnumber(route.getId(), maxSegmentNumber);
              List<Segment> segments=segmentRepository.findByRouteId(route.getId());
              
              List<Location> originLocations=new ArrayList<Location>();
              List<Location> destinationLocations=new ArrayList<Location>();
              List<String> originNames=new ArrayList<String>();
              List<String> destinationNames=new ArrayList<String>();
              for(Segment segment:segments){
              Location getOriginLocations=locationRepository.findByAssetId(segment.getAssetorigin().getId());
              Location getDestinationLocations=locationRepository.findByAssetId(segment.getAssetdestination().getId());
              
              originNames.add(segment.getAssetorigin().getName());
              destinationNames.add(segment.getAssetdestination().getName());
              routeUtil.setOriginNames(originNames);
              routeUtil.setDestinationNames(destinationNames);
              originLocations.add(getOriginLocations);
              destinationLocations.add(getDestinationLocations);
              routeUtil.setOriginLocations(originLocations);
              routeUtil.setDestinationLocations(destinationLocations);
              
              }
              double sum=0;
              double averageScore;
              ZonedDateTime lastmodifieddate=scoreRepository.findMaxLastmodifieddateByRouteId(route.getId());
              if(lastmodifieddate==null){
             	 averageScore=Double.NaN;
              }
             	 else {
             	 long runid=scoreRepository.findMaxRunid(lastmodifieddate,route.getId());
                  List<Score> scores=scoreRepository.findByRunidAndRouteId(runid, route.getId());
                  System.out.println(route.getId());
                  System.out.println(scores);
                  for(Score score:scores){
                 	 sum=sum+score.getValue();
                  }
                  averageScore=sum/scores.size();
              }
             
              
              routeUtil.setRouteId(route.getId());
              routeUtil.setRoutName(route.getName());
              routeUtil.setOriginName(firstSegment.getAssetorigin().getName());
              routeUtil.setDestinationName(lastSegment.getAssetdestination().getName());
              routeUtil.setAverageScore(averageScore);
              
              list.add(routeUtil);
         }
    
     		return list;
    }
    
    /**
     * GET -> index route.
     */
    @RequestMapping(value = "indexroute",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void add() {
        List<Route> routes = routeRepository.findAll();
        log.debug("In IndexResource.java");

        for (Route route:routes) {
            String id = route.getId().toString();
            IndexQuery indexQuery = new IndexQueryBuilder().withId(id).withObject(route).build();
            elasticsearchTemplate.index(indexQuery);
        }
    }
    
    /**
     * GET ->Execute filter route.
     */
    @RequestMapping(value = "executeRoutFilter/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<RouteUtil> elastic(HttpServletRequest request,@PathVariable long id) {
    	List<RouteUtil> list=new ArrayList<RouteUtil>();
    	Filter filter=filterRepository.findOne(id);
    	String query=filter.getQueryelastic();
    	log.info("Elastic query :" +query);
    	//QueryBuilder filterByDomain = termQuery("domain","DEMO"); 
    	BoolQueryBuilder bool = new BoolQueryBuilder()
        .must(new WrapperQueryBuilder(query));
        List<Route> data = Lists.newArrayList(routeSearchRepository.search(bool));
        long minSegmentNumber;
        long maxSegmentNumber;
        Segment firstSegment=null;
        Segment lastSegment=null;
     
        for(Route route:data){
        	 RouteUtil routeUtil=new RouteUtil();
          
        	 minSegmentNumber=segmentRepository.getMinSegmentnumberByRouteId(route.getId());
        	 maxSegmentNumber=segmentRepository.getMaxSegmentnumberByRouteId(route.getId());
        	 firstSegment=segmentRepository.findByRouteIdAndSegmentnumber(route.getId(), minSegmentNumber);
        	 lastSegment=segmentRepository.findByRouteIdAndSegmentnumber(route.getId(), maxSegmentNumber);
             List<Segment> segments=segmentRepository.findByRouteId(route.getId());
             
             List<Location> originLocations=new ArrayList<Location>();
             List<Location> destinationLocations=new ArrayList<Location>();
             List<String> originNames=new ArrayList<String>();
             List<String> destinationNames=new ArrayList<String>();
             for(Segment segment:segments){
             Location getOriginLocations=locationRepository.findByAssetId(segment.getAssetorigin().getId());
             Location getDestinationLocations=locationRepository.findByAssetId(segment.getAssetdestination().getId());
             
             originNames.add(segment.getAssetorigin().getName());
             destinationNames.add(segment.getAssetdestination().getName());
             routeUtil.setOriginNames(originNames);
             routeUtil.setDestinationNames(destinationNames);
             originLocations.add(getOriginLocations);
             destinationLocations.add(getDestinationLocations);
             routeUtil.setOriginLocations(originLocations);
             routeUtil.setDestinationLocations(destinationLocations);
             
             }
             double sum=0;
             double averageScore;
             ZonedDateTime lastmodifieddate=scoreRepository.findMaxLastmodifieddateByRouteId(route.getId());
             if(lastmodifieddate==null){
            	 averageScore=Double.NaN;
             }
            	 else {
            	 long runid=scoreRepository.findMaxRunid(lastmodifieddate,route.getId());
                 List<Score> scores=scoreRepository.findByRunidAndRouteId(runid, route.getId());
                 System.out.println(route.getId());
                 System.out.println(scores);
                 for(Score score:scores){
                	 sum=sum+score.getValue();
                 }
                 averageScore=sum/scores.size();
             }
            
             
             routeUtil.setRouteId(route.getId());
             routeUtil.setRoutName(route.getName());
             routeUtil.setOriginName(firstSegment.getAssetorigin().getName());
             routeUtil.setDestinationName(lastSegment.getAssetdestination().getName());
             routeUtil.setAverageScore(averageScore);
             
             list.add(routeUtil);
        }
   
    		return list;
    }
}
