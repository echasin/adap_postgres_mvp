package com.innvo.drools;

import java.util.HashMap;
import java.util.Map;

import org.drools.core.event.DebugProcessEventListener;
import org.kie.api.KieServices;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import com.innvo.domain.Route;
import com.innvo.domain.Score;
import com.innvo.web.rest.util.RouteUtil;

public class WorkFlowStart {
	
	public void startWorkFlow (long filterid)
	{
		String filterId=String.valueOf(filterid);
		try {
			// load up the knowledge base
			KieServices ks = KieServices.Factory.get();
			KieContainer kContainer = ks.getKieClasspathContainer();
			KieSession kSession = kContainer.newKieSession("ksession-process");
			kSession.addEventListener(new DebugProcessEventListener());
			kSession.addEventListener(new DebugAgendaEventListener());
			kSession.addEventListener(new DebugRuleRuntimeEventListener());
			KieRuntimeLogger logger = ks.getLoggers().newFileLogger(kSession, "./workflowlog");
			
			Map<String,Object> params = new HashMap<String,Object>();
			Route route=new Route();
			RouteUtil routeutil=new RouteUtil();
			Score score=new Score();
			WorkFlowExecutor workFlowExecutor=new WorkFlowExecutor();
            params.put("filterId",filterId);
            params.put("route", route);
            params.put("score", score);
            params.put("workFlowExecutor", workFlowExecutor);
            params.put("routeutil", routeutil);
            kSession.startProcess("innvoprocess",params);
			kSession.fireAllRules();
			kSession.dispose();
			} catch (Throwable t) {
			t.getMessage();
			t.printStackTrace();
		}


	}

}
