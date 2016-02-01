package com.innvo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.innvo.domain.Filter;
import com.innvo.domain.Route;
import com.innvo.domain.Score;
import com.innvo.domain.Segment;
import com.innvo.domain.User;
import com.innvo.domain.enumeration.Status;
import com.innvo.drools.RuleExecutor;
import com.innvo.drools.Todo;
import com.innvo.repository.FilterRepository;
import com.innvo.repository.ScoreRepository;
import com.innvo.repository.SegmentRepository;
import com.innvo.repository.UserRepository;
import com.innvo.repository.search.RouteSearchRepository;
import com.innvo.repository.search.ScoreSearchRepository;
import com.innvo.web.rest.util.HeaderUtil;
import com.innvo.web.rest.util.PaginationUtil;

import io.gatling.core.json.JSON;
import springfox.documentation.spring.web.json.Json;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.WrapperQueryBuilder;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Score.
 */
@RestController
@RequestMapping("/api")
public class ScoreResource {

    private final Logger log = LoggerFactory.getLogger(ScoreResource.class);

    @Inject
    private ScoreRepository scoreRepository;

    @Inject
    private ScoreSearchRepository scoreSearchRepository;
    
    @Inject
    RouteSearchRepository routeSearchRepository;
    

    @Inject
    UserRepository userRepository;
    
    @Inject
    ElasticsearchTemplate elasticsearchTemplate;
    
    @Inject
    FilterRepository filterRepository;
    
    @Inject
    SegmentRepository segmentRepository;
    
    long runId=0;
    
    /**
     * POST  /scores -> Create a new score.
     */
    @RequestMapping(value = "/scores",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Score> createScore(@Valid @RequestBody Score score,Principal principal) throws URISyntaxException {
        log.debug("REST request to save Score : {}", score);
        if (score.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new score cannot already have an ID").body(null);
        }
        ZonedDateTime lastmodifieddate = ZonedDateTime.now(ZoneId.systemDefault());
        User user = userRepository.findByLogin(principal.getName());
        score.setDomain(user.getDomain());
        score.setStatus(Status.Active);
        score.setLastmodifiedby(principal.getName());
        score.setLastmodifieddate(lastmodifieddate);
        Score result = scoreRepository.save(score);
        scoreSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/scores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("score", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scores -> Updates an existing score.
     */
    @RequestMapping(value = "/scores",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Score> updateScore(@Valid @RequestBody Score score,Principal principal) throws URISyntaxException {
        log.debug("REST request to update Score : {}", score);
        if (score.getId() == null) {
            return createScore(score,principal);
        }
        ZonedDateTime lastmodifieddate = ZonedDateTime.now(ZoneId.systemDefault());
        User user = userRepository.findByLogin(principal.getName());
        score.setDomain(user.getDomain());
        score.setStatus(Status.Active);
        score.setLastmodifiedby(principal.getName());
        score.setLastmodifieddate(lastmodifieddate);
        Score result = scoreRepository.save(score);
        scoreSearchRepository.save(score);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("score", score.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scores -> get all the scores.
     */
    @RequestMapping(value = "/scores",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Score>> getAllScores(Pageable pageable)
        throws URISyntaxException {
        Page<Score> page = scoreRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scores");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /scores -> get all the scores.
     */
    @RequestMapping(value = "/scores/{paginationOptions.pageNumber}/{paginationOptions.pageSize}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Score>> getAllSegments(HttpServletRequest request, Principal principal, @PathVariable("paginationOptions.pageNumber") String pageNumber,
            @PathVariable("paginationOptions.pageSize") String pageSize
    )
            throws URISyntaxException {
        int thepage = Integer.parseInt(pageNumber);
        int thepagesize = Integer.parseInt(pageSize);
        User user = userRepository.findByLogin(principal.getName());
        PageRequest pageRequest = new PageRequest(thepage, thepagesize, Sort.Direction.ASC, "id");
        Page<Score> data = scoreRepository.findByDomain(user.getDomain(), pageRequest);
        return new ResponseEntity<>(data.getContent(), HttpStatus.OK);
    }

    /**
     * GET /score/count -> Get Records Size
     */
    @RequestMapping(value = "/score/recordsLength",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public long getlength(Principal principal)
            throws URISyntaxException {
        User user = userRepository.findByLogin(principal.getName());
        long length = scoreRepository.countByDomain(user.getDomain());
        return length;
    }
    
    /**
     * GET  /scores/:id -> get the "id" score.
     */
    @RequestMapping(value = "/scores/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Score> getScore(@PathVariable Long id) {
        log.debug("REST request to get Score : {}", id);
        return Optional.ofNullable(scoreRepository.findOne(id))
            .map(score -> new ResponseEntity<>(
                score,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
     /**
     * GET  /scores/asset/:id -> get the "id" asset.
     */
    @RequestMapping(value = "/scores/asset/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Set<Score>> getScoreByAsset(@PathVariable Long id) {
        log.debug("REST request to get Score : {}", id);
        return Optional.ofNullable(scoreRepository.findByAssetId(id))
            .map(score -> new ResponseEntity<>(
                score,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * GET  /scores/averageScore/:id -> get averageScore by id.
     */
    @RequestMapping(value = "/averageScore/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Score>> getAverageScore(@PathVariable Long id) {
    	 double sum=0;
         double averageScore;
         List<Score> scores=new ArrayList<Score>();
         long runid;
         ZonedDateTime lastmodifieddate=scoreRepository.findMaxLastmodifieddateByRouteId(id);
         if(lastmodifieddate==null){
        	 averageScore=Double.NaN;
         }
        	 else {
        	 runid=scoreRepository.findMaxRunid(lastmodifieddate,id);
             scores=scoreRepository.findByRunidAndRouteId(runid, id);
             System.out.println(id);
             System.out.println(scores);
             for(Score score:scores){
            	 sum=sum+score.getValue();
             }
             averageScore=sum/scores.size();
         }
         return Optional.ofNullable(scores)
                 .map(score -> new ResponseEntity<>(
                     score,
                     HttpStatus.OK))
                 .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /scores/:id -> delete the "id" score.
     */
    @RequestMapping(value = "/scores/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteScore(@PathVariable Long id) {
        log.debug("REST request to delete Score : {}", id);
        scoreRepository.delete(id);
        scoreSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("score", id.toString())).build();
    }

    /**
     * SEARCH  /_search/scores/:query -> search for the score corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/scores/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Score> searchScores(@PathVariable String query,Principal principal) {
    	User user = userRepository.findByLogin(principal.getName());
    	QueryBuilder filterByDomain = termQuery("domain", user.getDomain()); 
    	QueryBuilder queryBuilder = queryStringQuery(query); 
    	BoolQueryBuilder bool = new BoolQueryBuilder()
    			.must(queryBuilder)
                .must(filterByDomain);

      	List<Score> result = Lists.newArrayList(scoreSearchRepository.search(bool));
        return result;
    }
    
    /**
     * GET -> index score.
     */
    @RequestMapping(value = "indexscore",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void add() {
        List<Score> scores = scoreRepository.findAll();
        log.debug("In IndexResource.java");

        for (Score score:scores) {
            String id = score.getId().toString();
            IndexQuery indexQuery = new IndexQueryBuilder().withId(id).withObject(score).build();
            elasticsearchTemplate.index(indexQuery);
        }
    }
    
    /**
     * GET   ->fireTestCaseOne.
     * @throws JSONException 
     */
    @RequestMapping(value = "/fireTestCaseOne",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
	public void fireTestCaseOne(HttpServletRequest request,Principal principal) throws JSONException {
    	Filter filter=filterRepository.findOne((long)524);
    	String query=filter.getQueryelastic();
    	System.out.println(query);
    	BoolQueryBuilder bool = new BoolQueryBuilder()
        .must(new WrapperQueryBuilder(query));
        List<Route> routes= Lists.newArrayList(routeSearchRepository.search(bool));
		Todo todo=new Todo();
		runId++;
        ZonedDateTime lastmodifieddate = ZonedDateTime.now(ZoneId.systemDefault());
        User user = userRepository.findByLogin(principal.getName());
		
		for(Route route:routes){
			List<Segment> segments=segmentRepository.findByRouteId(route.getId());
			todo.setValue(segments.size());
    		todo.setRoute(route);        
            todo.setRunId(runId);
		    todo.setStatus(Status.Active);
		    JSONObject details = new JSONObject();
		    details.put("ScenarioName", "Scenario_One");
		    details.put("ScoreCategoryName", "Category_One");
		    details.put("RuleName", "r_count_segments");
		    details.put("Value", segments.size());
		    todo.setDetails(details.toString());
		    todo.setDomain(user.getDomain());
		    todo.setLastmodifiedby(principal.getName());
		    todo.setLastmodifieddate(lastmodifieddate);
		    todo.setObjrecordtype(route.getObjrecordtype());
		    todo.setObjclassification(route.getObjclassification());
		    todo.setObjcategory(route.getObjcategory());   
		    RuleExecutor ruleExecutor = new RuleExecutor();
		    Score score= ruleExecutor.processRules(todo);
		    scoreRepository.save(score);
	 }
    }
    
    
    /**
     * GET   ->fireTestCaseTwo.
     * @throws JSONException 
     */
    @RequestMapping(value = "/fireTestCaseTwo",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
	public void fireTestCaseTwo(HttpServletRequest request,Principal principal) throws JSONException {
    	Filter filter=filterRepository.findOne((long)525);
    	String query=filter.getQueryelastic();
    	System.out.println(query);
    	BoolQueryBuilder bool = new BoolQueryBuilder()
        .must(new WrapperQueryBuilder(query));
        List<Route> routes= Lists.newArrayList(routeSearchRepository.search(bool));
		Todo todo=new Todo();
		runId++;
        ZonedDateTime lastmodifieddate = ZonedDateTime.now(ZoneId.systemDefault());
        User user = userRepository.findByLogin(principal.getName());
		
		for(Route route:routes){
			List<Segment> segments=segmentRepository.findByRouteId(route.getId());
			todo.setValue(segments.size());
    		todo.setRoute(route);        
            todo.setRunId(runId);
		    todo.setStatus(Status.Active);
		    JSONObject details = new JSONObject();
		    details.put("ScenarioName", "Scenario_One");
		    details.put("ScoreCategoryName", "Category_One");
		    details.put("RuleName", "r_count_segments");
		    details.put("Value", segments.size());
		    todo.setDetails(details.toString());
		    todo.setDomain(user.getDomain());
		    todo.setLastmodifiedby(principal.getName());
		    todo.setLastmodifieddate(lastmodifieddate);
		    todo.setObjrecordtype(route.getObjrecordtype());
		    todo.setObjclassification(route.getObjclassification());
		    todo.setObjcategory(route.getObjcategory());   
		    RuleExecutor ruleExecutor = new RuleExecutor();
		    Score score= ruleExecutor.processRules(todo);
		    scoreRepository.save(score);
	 }
    }
}
