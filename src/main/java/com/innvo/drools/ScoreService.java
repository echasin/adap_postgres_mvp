package com.innvo.drools;


import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.WrapperQueryBuilder;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.ObjectFilter;
import org.kie.api.runtime.rule.FactHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.Lists;
import com.innvo.domain.Filter;
import com.innvo.domain.Location;
import com.innvo.domain.Route;
import com.innvo.domain.Score;
import com.innvo.domain.Scorefactor;
import com.innvo.domain.Segment;
import com.innvo.domain.User;
import com.innvo.domain.enumeration.Status;
import com.innvo.repository.FilterRepository;
import com.innvo.repository.LocationRepository;
import com.innvo.repository.ScoreRepository;
import com.innvo.repository.ScorefactorRepository;
import com.innvo.repository.SegmentRepository;
import com.innvo.repository.UserRepository;
import com.innvo.repository.search.RouteSearchRepository;
import com.innvo.service.UserService;

import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ScoreService{

	    private final Logger log = LoggerFactory.getLogger(UserService.class);
	
	    
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

	    @Inject
	    LocationRepository locationRepository;

	    @Inject
	    ScorefactorRepository scorefactorRepository;

	    @Inject
	    ScoreRepository scoreRepository;
	    long runId = 0;
	    
		KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	KieSession kSession = kContainer.newKieSession("ksession-process");
	    
	public void  insertScoreFact(long filterId,Principal principal){
		   


		    	Filter filter = filterRepository.findOne(filterId);                                                                               
		    	User user = userRepository.findByLogin(principal.getName());                                         

		        String query = filter.getQueryelastic();
		        System.out.println(query);
		        BoolQueryBuilder bool = new BoolQueryBuilder()
		                .must(new WrapperQueryBuilder(query));
		        List<Route> routes = Lists.newArrayList(routeSearchRepository.search(bool));
		        ScoreRouteRulefile scoreRouteRulefile = new ScoreRouteRulefile();
		        runId++;
		        ZonedDateTime lastmodifieddate = ZonedDateTime.now(ZoneId.systemDefault());


		        List<Scorefactor> scorefactors = scorefactorRepository.findAll();
		        long minSegmentNumber;
		        Segment firstSegment = null;
		        
		       
		        
		        for (Route route : routes) {
		        	
		        	List<Segment> segments = segmentRepository.findByRouteId(route.getId());
		            minSegmentNumber = segmentRepository.getMinSegmentnumberByRouteId(route.getId());
		            firstSegment = segmentRepository.findByRouteIdAndSegmentnumber(route.getId(), minSegmentNumber);
		            Location location = locationRepository.findByAssetId(firstSegment.getAssetorigin().getId());
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
		            scoreRouteRulefile.setRulefilename("assddddd");
		            scoreRouteRulefile.setLocation(location);
		            scoreRouteRulefile.setScorefactors(scorefactors);
		            kSession.insert(scoreRouteRulefile);
		            
		        }
	             kSession.startProcess("com.innvo.drools", null);    
		         kSession.fireAllRules(); 
		         List<Score> scores=findScoreFact(kSession);
		         for(Score score:scores){
		           scoreRepository.save(score);
		         }         
  
	}

	
	private List<Score> findScoreFact(KieSession kieSession) {
	    
	    ObjectFilter scoreFilter = new ObjectFilter() {
	        @Override
	        public boolean accept(Object object) {
	            if (Score.class.equals(object.getClass())) return true;
	            if (Score.class.equals(object.getClass().getSuperclass())) return true;
	            return false;
	        }
	    };
	    List<Score> facts = new ArrayList<Score>();
	    for (FactHandle handle : kieSession.getFactHandles(scoreFilter)) {
	        facts.add((Score) kieSession.getObject(handle));
	    }
	    if (facts.size() == 0) {
	        return null;
	    }
	     return facts;
	}
	 
}
