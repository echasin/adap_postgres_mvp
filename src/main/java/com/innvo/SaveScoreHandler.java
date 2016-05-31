package com.innvo;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.innvo.domain.Score;
import com.innvo.drools.SaveScoreExecutor;

public class SaveScoreHandler implements WorkItemHandler {
	public static Score scoreVal;

	public SaveScoreHandler() {
		// empty
	}

	public SaveScoreHandler(Score scoreVal) {
		SaveScoreHandler.scoreVal = scoreVal;
	}

	private final Logger log = LoggerFactory.getLogger(SaveScoreHandler.class);

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		manager.abortWorkItem(workItem.getId());
	}

	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		log.info("Score Value from drl file :" + scoreVal);
		String hostName = (String) workItem.getParameter("hostname");
		log.info("Hostname in SaveScoreHandler :" + hostName);
		SaveScoreExecutor saveScoreExecutor = new SaveScoreExecutor();
		try {
			saveScoreExecutor.saveScore(scoreVal, hostName);
		} catch (Exception e) {
			 log.error("Threw a Exception in SaveScoreHandler::executeWorkItem, full stack trace follows:", e);
		} 

	}

}
