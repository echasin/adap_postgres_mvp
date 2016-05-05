package com.innvo.drools;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.innvo.domain.Score;
import com.innvo.web.rest.util.RESTClient;
import com.innvo.web.rest.util.RESTClientException;

public class ScoreExecutor {
	JSONObject jsonObject;

	private final Logger log = LoggerFactory.getLogger(ScoreExecutor.class);

	RESTClient restClient = null;

	public void saveScore(Score score) throws RESTClientException, IOException, JSONException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			String json = ow.writeValueAsString(score);//
			log.info("JSON DATA :" + json);
			restClient = new RESTClient();
			String token = restClient.getToken();
			String response = restClient.getInstance().request(RESTClient.RestHttpMethod.POST,
					"http://127.0.0.1:8099/api/scores", null, json, token);
			log.info("Rest response After  Save Score" + ": " + response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}

	}
}
