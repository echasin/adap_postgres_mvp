package com.innvo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.google.common.collect.Lists;
import com.innvo.FindRouteHandler;
import com.innvo.GetRouteByRouteIDHandler;
import com.innvo.domain.Filter;
import com.innvo.domain.Route;
import com.innvo.domain.Score;
import com.innvo.domain.Segment;
import com.innvo.domain.User;
import com.innvo.domain.enumeration.Status;
import com.innvo.drools.RuleExecutor;
import com.innvo.drools.ScoreRouteRulefile;
import com.innvo.repository.FilterRepository;
import com.innvo.repository.ScoreRepository;
import com.innvo.repository.SegmentRepository;
import com.innvo.repository.UserRepository;
import com.innvo.repository.search.RouteSearchRepository;
import com.innvo.repository.search.ScoreSearchRepository;
import com.innvo.web.rest.util.HeaderUtil;
import com.innvo.web.rest.util.PaginationUtil;

import org.apache.commons.io.FilenameUtils;
import org.drools.core.event.DebugProcessEventListener;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.WrapperQueryBuilder;
import org.jbpm.workflow.instance.WorkflowRuntimeException;
import org.json.JSONException;
import org.kie.api.KieServices;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	long runId = 0;

	/**
	 * POST /scores -> Create a new score.
	 */
	@RequestMapping(value = "/scores", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Score> createScore(@Valid @RequestBody Score score, Principal principal)
			throws URISyntaxException {
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
				.headers(HeaderUtil.createEntityCreationAlert("score", result.getId().toString())).body(result);
	}

	/**
	 * PUT /scores -> Updates an existing score.
	 */
	@RequestMapping(value = "/scores", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Score> updateScore(@Valid @RequestBody Score score, Principal principal)
			throws URISyntaxException {
		log.debug("REST request to update Score : {}", score);
		if (score.getId() == null) {
			return createScore(score, principal);
		}
		ZonedDateTime lastmodifieddate = ZonedDateTime.now(ZoneId.systemDefault());
		User user = userRepository.findByLogin(principal.getName());
		score.setDomain(user.getDomain());
		score.setStatus(Status.Active);
		score.setLastmodifiedby(principal.getName());
		score.setLastmodifieddate(lastmodifieddate);
		Score result = scoreRepository.save(score);
		scoreSearchRepository.save(score);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("score", score.getId().toString()))
				.body(result);
	}

	/**
	 * GET /scores -> get all the scores.
	 */
	@RequestMapping(value = "/scores", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Score>> getAllScores(Pageable pageable) throws URISyntaxException {
		Page<Score> page = scoreRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scores");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /scores -> get all the scores.
	 */
	@RequestMapping(value = "/scores/{paginationOptions.pageNumber}/{paginationOptions.pageSize}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Score>> getAllSegments(HttpServletRequest request, Principal principal,
			@PathVariable("paginationOptions.pageNumber") String pageNumber,
			@PathVariable("paginationOptions.pageSize") String pageSize) throws URISyntaxException {
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
	@RequestMapping(value = "/score/recordsLength", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public long getlength(Principal principal) throws URISyntaxException {
		User user = userRepository.findByLogin(principal.getName());
		long length = scoreRepository.countByDomain(user.getDomain());
		return length;
	}

	/**
	 * GET /scores/:id -> get the "id" score.
	 */
	@RequestMapping(value = "/scores/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Score> getScore(@PathVariable Long id) {
		log.debug("REST request to get Score : {}", id);
		return Optional.ofNullable(scoreRepository.findOne(id)).map(score -> new ResponseEntity<>(score, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * GET /scores/asset/:id -> get the "id" asset.
	 */
	@RequestMapping(value = "/scores/asset/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Set<Score>> getScoreByAsset(@PathVariable Long id) {
		log.debug("REST request to get Score : {}", id);
		return Optional.ofNullable(scoreRepository.findByAssetId(id))
				.map(score -> new ResponseEntity<>(score, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * GET /scores/averageScore/:id -> get averageScore by id.
	 */
	@RequestMapping(value = "/averageScore/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Score>> getAverageScore(@PathVariable Long id) {
		double sum = 0;
		double averageScore;
		List<Score> scores = new ArrayList<Score>();
		long runid;
		ZonedDateTime lastmodifieddate = scoreRepository.findMaxLastmodifieddateByRouteId(id);
		if (lastmodifieddate == null) {
			averageScore = Double.NaN;
		} else {
			runid = scoreRepository.findMaxRunid(lastmodifieddate, id);
			scores = scoreRepository.findByRunidAndRouteId(runid, id);
			System.out.println(id);
			System.out.println(scores);
			for (Score score : scores) {
				sum = sum + score.getValue();
			}
			averageScore = sum / scores.size();
		}
		return Optional.ofNullable(scores).map(score -> new ResponseEntity<>(score, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /scores/:id -> delete the "id" score.
	 */
	@RequestMapping(value = "/scores/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deleteScore(@PathVariable Long id) {
		log.debug("REST request to delete Score : {}", id);
		scoreRepository.delete(id);
		scoreSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("score", id.toString())).build();
	}

	/**
	 * SEARCH /_search/scores/:query -> search for the score corresponding to
	 * the query.
	 */
	@RequestMapping(value = "/_search/scores/{query}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Score> searchScores(@PathVariable String query, Principal principal) {
		User user = userRepository.findByLogin(principal.getName());
		QueryBuilder filterByDomain = termQuery("domain", user.getDomain());
		QueryBuilder queryBuilder = queryStringQuery(query);
		BoolQueryBuilder bool = new BoolQueryBuilder().must(queryBuilder).must(filterByDomain);

		List<Score> result = Lists.newArrayList(scoreSearchRepository.search(bool));
		return result;
	}

	/**
	 * GET -> index score.
	 */
	@RequestMapping(value = "indexscore", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public void add() {
		List<Score> scores = scoreRepository.findAll();
		log.debug("In IndexResource.java");

		for (Score score : scores) {
			String id = score.getId().toString();
			IndexQuery indexQuery = new IndexQueryBuilder().withId(id).withObject(score).build();
			elasticsearchTemplate.index(indexQuery);
		}
	}

	/**
	 * GET ->get rules.
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/getRules", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<String> getRules(HttpServletRequest request, Principal principal) throws JSONException, IOException {
		List<String> rulesName = new ArrayList<String>();

		String path = request.getSession().getServletContext().getRealPath("/rules");
		File directory = new File(path);
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				String fileNameWithOutExt = FilenameUtils.removeExtension(file.getName());
				System.out.println(fileNameWithOutExt);
				rulesName.add(fileNameWithOutExt);
			}
		}
		return rulesName;
	}

	/**
	 * GET ->get process.
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/getWorkFlows", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<String> getWorkFlows(HttpServletRequest request, Principal principal)
			throws JSONException, IOException {
	List<String> workFlowsName = new ArrayList<String>();
	 String path = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/process");
     File directory = new File(path);       
     File[] fList = directory.listFiles();
     for (File file : fList){
     	
         if (file.isFile()){
         	
          if(!file.getName().contains(".drl")){
         	String fileNameWithOutExt = FilenameUtils.removeExtension(file.getName());
             log.info("WorkFlow/Process ID : "+fileNameWithOutExt);
             workFlowsName.add(fileNameWithOutExt);
         	}
         }
        
     }
     
     return workFlowsName;
	}

	/**
	 * GET ->fireTestCaseOne.
	 * 
	 * @throws JSONException
	 */
	@RequestMapping(value = "/fireRules/{filterId}/{fileName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public void fireTestCaseOne(@PathVariable("filterId") long filterId, @PathVariable("fileName") String fileName,
			HttpServletRequest request, Principal principal) throws JSONException {

		Filter filter = filterRepository.findOne(filterId);
		String query = filter.getQueryelastic();
		BoolQueryBuilder bool = new BoolQueryBuilder().must(new WrapperQueryBuilder(query));
		List<Route> routes = Lists.newArrayList(routeSearchRepository.search(bool));
		ScoreRouteRulefile scoreRouteRulefile = new ScoreRouteRulefile();
		runId++;
		ZonedDateTime lastmodifieddate = ZonedDateTime.now(ZoneId.systemDefault());
		User user = userRepository.findByLogin(principal.getName());

		for (Route route : routes) {
			List<Segment> segments = segmentRepository.findByRouteId(route.getId());
			scoreRouteRulefile.setValue(segments.size());
			scoreRouteRulefile.setRoute(route);
			scoreRouteRulefile.setRunId(runId);
			scoreRouteRulefile.setStatus(Status.Active);
			scoreRouteRulefile.setDomain(user.getDomain());
			scoreRouteRulefile.setLastmodifiedby(principal.getName());
			scoreRouteRulefile.setLastmodifieddate(lastmodifieddate);
			scoreRouteRulefile.setObjrecordtype(route.getObjrecordtype());
			scoreRouteRulefile.setObjclassification(route.getObjclassification());
			scoreRouteRulefile.setObjcategory(route.getObjcategory());
			scoreRouteRulefile.setRulefilename(fileName);
			try {
				RuleExecutor ruleExecutor = new RuleExecutor();
				Score score = ruleExecutor.processRules(scoreRouteRulefile, "group one", fileName);
				scoreRepository.save(score);
				System.out.println(score);
				System.out.println(score.getRulefilename());
				System.out.println(score.getRulename());
				System.out.println(score.getObjtype());
			} catch (InvalidDataAccessApiUsageException e) {
				/// e.printStackTrace();
			}
		}
	}

	/**
	 * GET ->Start the workflow in turn fire the rules.
	 * 
	 * @return
	 * @throws JSONException
	 * @throws FileNotFoundException 
	 * @throws YamlException 
	 */
	@RequestMapping(value = "/workFlow/{filterId}/{fileName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public String startWorkFlow(@PathVariable("filterId") long filterId, @PathVariable("fileName") String fileName,
			HttpServletRequest request, Principal principal)
			throws JSONException, FileNotFoundException, YamlException {
		String result = null;
		log.info("Pass Filter ID and Process ID in Process to get started : " + filterId + "\t " + fileName);
		result = "{\"Route Found\":\"SUCCESS\"}";
		log.info("Pass Filter ID and Process ID in Process to get started : " + filterId + "\t " + fileName);
		String path = request.getSession().getServletContext()
				.getRealPath("/WEB-INF/classes/config/application-dev.yml");
		YamlReader reader = new YamlReader(new FileReader(path));
		Object fileContent = reader.read();
		log.info("Yml file content :" + fileContent);
		Map map = (Map) fileContent;
		log.info("Hostname in score resource :" + map.get("hostname"));
		String hostName = map.get("hostname").toString();
		try {
			// load up the knowledge base
			KieServices ks = KieServices.Factory.get();
			KieContainer kContainer = ks.getKieClasspathContainer();
			KieSession kSession = kContainer.newKieSession("ksession-process");
			kSession.getWorkItemManager().registerWorkItemHandler("FindRouteHandler", new FindRouteHandler());
			kSession.addEventListener(new DebugProcessEventListener());
			kSession.addEventListener(new DebugAgendaEventListener());
			kSession.addEventListener(new DebugRuleRuntimeEventListener());
			KieRuntimeLogger logger = ks.getLoggers().newFileLogger(kSession, "./workflowlog");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("filterid", filterId);
			params.put("hostname", hostName);
			kSession.startProcess(fileName, params);
			kSession.fireAllRules();
			kSession.dispose();
		} catch (WorkflowRuntimeException wfre) {

			String msg = "An exception happened in "

					+ "process instance [" + wfre.getProcessInstanceId()

					+ "] of process [" + wfre.getProcessId()

					+ "] in node [id: " + wfre.getNodeId()

					+ ", name: " + wfre.getNodeName()

					+ "] and variable " + "Filter ID" + " had the value [" + wfre.getVariables().get("filterId")

					+ "]";

			log.warn("workflow runtime exception caught when passing filter id as " + msg);
			result = "{\"No Route Found\":\"" + msg + "\"}";
		}
		return result;
	}
	
	
	/**
	 * GET ->Start the routeworkflow in turn fire the rules.
	 * 
	 * @return
	 * @throws JSONException
	 * @throws FileNotFoundException 
	 * @throws YamlException 
	 */
	@RequestMapping(value = "/workFlowRoute/{routeId}/{fileName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public String startWorkFlowRoute(@PathVariable("routeId") long routeId, @PathVariable("fileName") String fileName,
			HttpServletRequest request, Principal principal)
			throws JSONException, FileNotFoundException, YamlException {
		String routeResult = null;
		log.info("Pass Route ID and Process ID in Process to get started : " + routeId + "\t " + fileName);
		routeResult = "{\"Route Found Value\":\"SUCCESS\"}";
		log.info("Pass Route ID and Process ID in Process to get started : " + routeId + "\t " + fileName);
		String path = request.getSession().getServletContext()
				.getRealPath("/WEB-INF/classes/config/application-dev.yml");
		YamlReader reader = new YamlReader(new FileReader(path));
		Object fileContent = reader.read();
		log.info("Yml file content :" + fileContent);
		Map map = (Map) fileContent;
		log.info("Hostname in score resource :" + map.get("hostname"));
		String hostName = map.get("hostname").toString();
		try {
			// load up the knowledge base
			KieServices ks = KieServices.Factory.get();
			KieContainer kContainer = ks.getKieClasspathContainer();
			KieSession kSession = kContainer.newKieSession("ksession-process");
			kSession.getWorkItemManager().registerWorkItemHandler("GetRouteByRouteIDHandler", new GetRouteByRouteIDHandler());
			kSession.addEventListener(new DebugProcessEventListener());
			kSession.addEventListener(new DebugAgendaEventListener());
			kSession.addEventListener(new DebugRuleRuntimeEventListener());
			ks.getLoggers().newFileLogger(kSession, "./workflowlog");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("routeId", routeId);
			params.put("hostname", hostName);
			kSession.startProcess(fileName, params);
			kSession.fireAllRules();
			kSession.dispose();
		} catch (WorkflowRuntimeException wfre) {

			String msg = "An exception happened in "

					+ "process instance [" + wfre.getProcessInstanceId()

					+ "] of process [" + wfre.getProcessId()

					+ "] in node [id: " + wfre.getNodeId()

					+ ", name: " + wfre.getNodeName()

					+ "] and variable " + "Route ID" + " had the value [" + wfre.getVariables().get("RouteId")

					+ "]";

			log.warn("workflow runtime exception caught when passing route id as " + msg);
			routeResult = "{\"No Route Found Value\":\"" + msg + "\"}";
		}
		return routeResult;
	}
}