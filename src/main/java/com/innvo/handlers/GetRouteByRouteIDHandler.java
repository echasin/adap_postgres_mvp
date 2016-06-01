package com.innvo.handlers;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.innvo.domain.Route;
import com.innvo.web.rest.util.RESTClient;

/**
 * This is a sample file to launch a process.
 */
public class GetRouteByRouteIDHandler implements WorkItemHandler {
	private final Logger log = LoggerFactory.getLogger(GetRouteByRouteIDHandler.class);

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		manager.abortWorkItem(workItem.getId());
	}

	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {

		log.info("Route ID :" + workItem.getParameter("routeId"));
		String routeIdVal = String.valueOf(workItem.getParameter("routeId"));
		String hostName = (String) workItem.getParameter("hostname");
		log.info("hostname :" + hostName);
		Route routeObj = new Route();
		routeObj = restCall(routeIdVal, hostName);
		String routeId = String.valueOf(routeObj.getId());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("route", routeObj);
		params.put("routeId", routeId);
		params.put("hostname", hostName);
		manager.completeWorkItem(workItem.getId(), params);
	}

	public Route restCall(String routeIdVal, String hostName) {
		RESTClient restClient = null;
		Route routeVal = new Route();
		if (routeIdVal != null) {

			try {
				restClient = new RESTClient();
				String token = restClient.getToken(hostName);
				String response = restClient.getJson("http://" + hostName + "/api/routes/" + routeIdVal, token);
				log.debug("Rest response for route id in GetRouteByRouteIDHandler :" + routeIdVal + ": " + response);
				JSONObject object = new JSONObject(response);
				routeVal.setId(object.getLong("id"));
				routeVal.setDescription(object.getString("description"));


				return routeVal;
			}

			catch (Exception e) {
				log.error(e.getMessage());
			}

		}
		return routeVal;

	}

}