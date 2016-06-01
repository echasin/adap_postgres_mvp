package com.innvo.handlers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.innvo.web.rest.util.RESTClient;
import com.innvo.web.rest.util.RouteUtil;


/**
 * This is a sample file to launch a process.
 */
public class FindRouteHandler implements WorkItemHandler {
	private final Logger log = LoggerFactory.getLogger(FindRouteHandler.class);

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		manager.abortWorkItem(workItem.getId());
	}

	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {

		log.info("Filter ID :" + workItem.getParameter("filterid"));
		String filterId = String.valueOf(workItem.getParameter("filterid"));
		String hostName = (String) workItem.getParameter("hostname");
		log.info("hostname :" + hostName);
		RouteUtil routeutil = new RouteUtil();
		routeutil = restCall(filterId, hostName);
		String routeId = String.valueOf(routeutil.getRouteId());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("routeutil", routeutil);
		params.put("routeId", routeId);
		params.put("hostname", hostName);
		manager.completeWorkItem(workItem.getId(), params);
	}

	public RouteUtil restCall(String filterid, String hostName) {
		RESTClient restClient = null;
		RouteUtil routeUtil = new RouteUtil();
		if (filterid != null) {

			try {
				restClient = new RESTClient();
				String token = restClient.getToken(hostName);
				String response = restClient.getJson("http://" + hostName + "/api/executeRoutFilter/" + filterid, token);
				log.debug("Rest response for route id in FindRouteHandler" + filterid + ": " + response);

				JSONArray array = new JSONArray(response);

				for (int n = 0; n < array.length(); n++) {
					JSONObject object = array.getJSONObject(n);

					routeUtil.setRouteId(object.getLong("routeId"));
					routeUtil.setRoutName(object.getString("routName"));
					routeUtil.setOriginName(object.getString("originName"));
					routeUtil.setDestinationName(object.getString("destinationName"));
					routeUtil.setSegmentName(object.getString("segmentName"));
					routeUtil.setAverageScore(object.getDouble("averageScore"));

					List originLocationsList = Arrays.asList(object.getString("originLocations"));
					routeUtil.setOriginLocations(originLocationsList);

					List destinationLocationsList = Arrays.asList(object.getString("destinationLocations"));
					routeUtil.setDestinationLocations(destinationLocationsList);

					List originNamesList = Arrays.asList(object.getString("originNames"));
					routeUtil.setOriginNames(originNamesList);

					List destinationNamesList = Arrays.asList(object.getString("destinationNames"));
					routeUtil.setDestinationNames(destinationNamesList);

				}

				return routeUtil;
			}

			catch (Exception e) {
				log.error(e.getMessage());
			}

		}
		return routeUtil;

	}

}