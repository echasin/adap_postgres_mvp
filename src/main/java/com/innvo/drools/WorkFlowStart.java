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

public class WorkFlowStart {
	
	public void startWorkFlow (long filterid,String fileName)
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
			params.put("filterId",filterId);
            kSession.startProcess(fileName,params);
			kSession.fireAllRules();
			kSession.dispose();
			} catch (Throwable t) {
			t.getMessage();
			t.printStackTrace();
		}


	}

}
