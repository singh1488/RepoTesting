package com.task;

import java.io.File;
import java.io.FileWriter;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.api.ExtractionApi;
import com.util.Utils;

public class UserProfileLoader {
	private String rootDirLocation;
	private String url;
	private String apiVersion;
	private String session;
	private Properties properties;

	public UserProfileLoader(String rootDirLocation, String url, String apiVersion, String session, Properties properties) {
		super();
		this.rootDirLocation = rootDirLocation;
		this.url = url;
		this.apiVersion = apiVersion;
		this.session = session;
		this.properties = properties;
	}

	@SuppressWarnings("unchecked")
	public boolean downloadUserProfile(Logger logger) {
		boolean status = false;

		JSONArray ja = new JSONArray();
		ja.add("id");
		ja.add("email__sys");
		ja.add("username__sys");
		ja.add("first_name__sys");
		ja.add("last_name__sys");
		ja.add("federated_id__sys");
		ja.add("security_profile__sys.name__v");
		ja.add("security_policy__sys.name__v");
		ja.add("activation_date__sys");
		ja.add("entity__c.name__v");
		ja.add("entity__c.entity_code__c");

		JSONArray mainJA = new JSONArray();
		JSONObject jo = new JSONObject();
		jo.put("object_type", "vobjects__v");
		jo.put("object", "user__sys");
		jo.put("fields", ja);
		jo.put("vql_criteria__v", "last_login__sys != null and status__v = 'active__v'");
		mainJA.add(jo);
		String json = mainJA.toJSONString();

		JSONObject jsonObject = ExtractionApi.createLoaderJob(session, url, logger, apiVersion, json);

		if (jsonObject != null) {
			
			long jobId = Long.parseLong(jsonObject.get("job_id").toString());
			JSONArray jsonArray = (JSONArray) jsonObject.get("tasks");
			JSONObject jsonObj = (JSONObject) jsonArray.get(0);
			String taskId = (String) jsonObj.get("task_id");

			boolean jobStatus = false;
			String downloadCsv = null;
			
			logger.info("User Profile Loader Created With JobId = "+jobId+" and TaskId = "+taskId);
			System.out.println("User Profile Loader Created With JobId = "+jobId+" and TaskId = "+taskId);
			
			logger.info("Checking Job Compilation JobId = "+jobId+" and TaskId = "+taskId);
			System.out.println("Checking Job Compilation JobId = "+jobId+" and TaskId = "+taskId);
			
			do {
				downloadCsv = ExtractionApi.downloadCsv(session, url, logger, apiVersion,
						jobId, taskId);
				
				

				if (downloadCsv != null) {
					if (downloadCsv.contains("FAILURE")) {
						jobStatus = false;
					} else {
						jobStatus = true;
					}

				}

			} while (!jobStatus);
			
			
			logger.info("Job Completed JobId = "+jobId+" and TaskId = "+taskId);
			System.out.println("Job Completed Compilation JobId = "+jobId+" and TaskId = "+taskId);

			if (jobStatus) {
				
				String dirName = Utils.getLoaderDirName(properties);
				
				File folder = new File(rootDirLocation+"\\"+dirName);

				if (!folder.exists()) {
					folder.mkdir();
				}
				
				status = createFile(downloadCsv, folder, logger);;
			}
		}

		return status;
	}
	
	public boolean createFile(String auditResponse, File directory, Logger logger) {

		String dacCsvFileName = Utils.getUserProfileLoaderFileName(properties);
		boolean status = false;

		try {

			String dacCsvFilePath = directory + "//" + dacCsvFileName;
			File file = new File(dacCsvFilePath);
			FileWriter wr = new FileWriter(file);
			wr.write(auditResponse);
			wr.flush();
			wr.close();
			status = true;
		} catch (Exception e) {
			status = false;
			logger.info("UserProfileLoader createFile Error - " + ExceptionUtils.getStackTrace(e) + "," + e.toString());
		}
		
		return status;
	}

}
