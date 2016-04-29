package com.innvo.drools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.innvo.web.rest.util.RESTClient;
import com.innvo.web.rest.util.RouteUtil;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class WorkFlowRouteExecutor {

	JSONObject jsonObject;

	private final Logger log = LoggerFactory.getLogger(WorkFlowRouteExecutor.class);

	RESTClient restClient = null;

	public RouteUtil restCall(String filterid) {
		RouteUtil routeUtil = new RouteUtil();
		if (filterid != null) {

			try {
				restClient = new RESTClient();
				String token = restClient.getToken();
				String response = restClient.getJson("http://127.0.0.1:8099/api/executeRoutFilter/" + filterid, token);
				log.debug("Rest response for route id" + filterid + ": " + response);

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
