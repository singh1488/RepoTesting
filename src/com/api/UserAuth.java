package com.api;

import java.util.logging.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserAuth {

	public static String userValidation(String userName, String password, String urlValue, Logger logger,
			String apiVersion) {

		String userAvailableorNot = "";
		String sessionId = null;

		try {
			String url = urlValue + apiVersion + "auth";
			
			logger.info("userValidation Url = "+url);

			OkHttpClient client = new OkHttpClient();

			RequestBody formBody = new FormBody.Builder().add("username", userName).add("password", password).build();

			Request request = new Request.Builder().url(url).post(formBody)
					.addHeader("Content-Type", "application/x-www-form-urlencoded").build();
			
			Response response = client.newCall(request).execute();

			userAvailableorNot = response.body().string();

			JSONParser parser = new JSONParser();
			Object responseResult = parser.parse(userAvailableorNot);
			JSONObject jo = (JSONObject) responseResult;
			String status = (String) jo.get("responseStatus");

			if (status.equalsIgnoreCase("SUCCESS")) {
				sessionId = (String) jo.get("sessionId");
			}

		} catch (Exception e) {
			logger.info("Exception in userValidation method : "+ ExceptionUtils.getStackTrace(e) + "," + e.toString());
		}

		return sessionId;
	}
	
	
	public  static boolean isSessionAlive(String vaultURL, String vaultSession, Logger logger, String apiVersion) {

		Request request = null;
		Response response = null;
		boolean sessionAlive = false;
		if (vaultSession == null) {
			return false;
		}
		
		String url = vaultURL + apiVersion + "objects/users/me";
		OkHttpClient client = new OkHttpClient();
		request = new Request.Builder().url(url).addHeader("Authorization", vaultSession).get()
				.build();
		String jasonResponse = null;
		
		try {
			response = client.newCall(request).execute();
			jasonResponse = response.body().string();
			JSONParser parser = new JSONParser();
			Object responseResult = parser.parse(jasonResponse);
			JSONObject jo = (JSONObject) responseResult;
			String status = (String) jo.get("responseStatus");
			if (status.equalsIgnoreCase("success")) {
				sessionAlive = true;

			} else {
				sessionAlive = false;
			}
		} catch (Exception e) {
			logger.info("Exception in isSessionAlive method : "+ ExceptionUtils.getStackTrace(e) + "," + e.toString());			
		} finally {
			if (response != null) {
				response.close();
			}
		}

		return sessionAlive;
	}

}
