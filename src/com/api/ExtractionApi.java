package com.api;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ExtractionApi {

	public static JSONObject createLoaderJob(String sessionId, String urlValue, Logger logger, String apiVersion,
			String jsonValue) {

		Request request = null;
		Response response = null;
		String responseResult = null;
		String url = null;
		JSONObject retJsonObject = null;

		try {
			url = urlValue + apiVersion + "services/loader/extract?sendNotification=true";

			OkHttpClient client = new OkHttpClient();
			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			builder.connectTimeout(60, TimeUnit.SECONDS);
			builder.readTimeout(240, TimeUnit.SECONDS);
			builder.writeTimeout(30, TimeUnit.SECONDS);
			client = builder.build();

			MediaType mediaType = MediaType.parse("application/json");

			RequestBody body = RequestBody.create(mediaType, jsonValue);
			request = new Request.Builder().url(url).method("POST", body).addHeader("Accept", "*/*")
					.addHeader("Authorization", sessionId).addHeader("Content-Type", "application/json").build();

			response = client.newCall(request).execute();

			responseResult = response.body().string();

			int responseStatus = response.code();

			if (responseStatus == 200) {

				Object objResponse = null;
				objResponse = new JSONParser().parse(responseResult);
				JSONObject jsonObject = (JSONObject) objResponse;
				String status = (String) jsonObject.get("responseStatus");

				if (status.equalsIgnoreCase("SUCCESS")) {
					retJsonObject = jsonObject;
				}
			}

		} catch (Exception e) {
			logger.info(
					"***************Exception in Class Name - ExtractionApi, Method Name - loadLoaderFile*****************");
			logger.info("ExtractionApi- loadLoaderFile, url = " + url);
			logger.info("ExtractionApi- loadLoaderFile, sessionId = " + sessionId);
			logger.info("ExtractionApi- loadLoaderFile, Response = " + responseResult);
			logger.info("ExtractionApi loadLoaderFile Error = " + ExceptionUtils.getStackTrace(e) + "," + e.toString());
			e.printStackTrace();
			logger.info(
					"**************************************************END**************************************************");

		} finally {
			response.close();
		}
		return retJsonObject;
	}

	public static String downloadCsv(String sessionId, String urlValue, Logger logger, String apiVersion, long jobid,
			String taskid) {

		Request request = null;
		Response response = null;
		String responseResult = null;
		String url = null;

		try {
			url = urlValue + apiVersion + "services/loader/" + jobid + "/tasks/" + taskid + "/results";

			OkHttpClient client = new OkHttpClient();
			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			builder.connectTimeout(60, TimeUnit.SECONDS);
			builder.readTimeout(240, TimeUnit.SECONDS);
			builder.writeTimeout(30, TimeUnit.SECONDS);
			client = builder.build();

			request = new Request.Builder().url(url).method("GET", null).addHeader("Content-Type", "text/plain")
					.addHeader("Authorization", sessionId).build();

			response = client.newCall(request).execute();
			responseResult = response.body().string();

		} catch (Exception e) {
			logger.info(
					"***************Exception in Class Name - ExtractionApi, Method Name - downloadCsv*****************");
			logger.info("ExtractionApi- downloadCsv, url = " + url);
			logger.info("ExtractionApi- downloadCsv, sessionId = " + sessionId);
			logger.info("ExtractionApi- downloadCsv, Response = " + responseResult);
			logger.info("ExtractionApi- downloadCsv Error = " + ExceptionUtils.getStackTrace(e) + "," + e.toString());
			e.printStackTrace();
			logger.info(
					"**************************************************END**************************************************");

		} finally {
			response.close();
		}
		return responseResult;
	}

}
