package com.connecttocare;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.api.UserAuth;
import com.task.UserProfileLoader;
import com.util.Utils;

/**
 * ConnectToCareApp is a console application which create a user report connect
 * to care
 *
 * @author Cognizant Sanofi Factory Team
 */

public class ConnectToCareApp {

	static FileHandler fh;
	static Logger logger = Logger.getLogger("MyLog");
	private Properties properties;
	private String rootDirLocation;
	private String mode;
	private String url;
	private String apiVersion;
	private String userName;
	private String password;
	private String session;

	{
		try {

			String rootDir = System.getProperty("user.dir");

			String logFilePath = "";

			File logFolder = new File(rootDir + "//" + "log");
			if (!logFolder.exists()) {
				logFolder.mkdir();
			}

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			Date date = new Date();
			String timestamp = format.format(date);

			String fileName = logFolder + "/" + timestamp + "_logInfo.log";
			File file = new File(fileName);
			if (file.createNewFile()) {
				logFilePath = fileName;
			} else {
				logger.info("Already exist");
			}

			fh = new FileHandler(logFilePath);
			logger.addHandler(fh);
			fh.setFormatter(new Formatter() {
				@Override
				public String format(LogRecord record) {
					StringBuilder sb = new StringBuilder();
					sb.append(record.getLevel()).append(':');
					sb.append(record.getMessage()).append('\n');
					return sb.toString();
				}
			});

		} catch (Exception e) {
			logger.info("LoginFrame" + e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Runs the application
	 *
	 * @param args
	 *            an array of String and @param params an array of String
	 * @throws InterruptedException
	 */

	public void run(String[] args, String[] params) {

		logger.info("Application Started...... ");

		/**
		 * Get root directory location and mode from params array
		 */
		rootDirLocation = params[0];
		mode = params[1];

		logger.info("rootDirLocation : " + rootDirLocation);
		logger.info("mode : " + mode);

		try {
			properties = Utils.readPropertiesFile(rootDirLocation);
		} catch (IOException e) {
			logger.info("Exception in ConnectToCareApp- run method : " + ExceptionUtils.getStackTrace(e) + ","
					+ e.toString());
			e.printStackTrace();
		}

		// List<String> arrlist = Arrays.asList("1");

		if (mode.equals("1")) {

			url = Utils.getApiUrl(properties);
			apiVersion = Utils.getApiVersion(properties);
			userName = Utils.getUserName(properties);
			password = Utils.getPassword(properties);

			session = UserAuth.userValidation(userName, password, url, logger, apiVersion);

			if (session == null) {
				logger.info("Invalid session id please check username and password.");
			} else {
				logger.info("Session id = " + session);
				System.out.println("Session id = " + session);
			}

		}

		/**
		 * Initializing the ExecutorService to run the loader process in
		 * parallel on 5 loader at a time
		 */
		if (mode.equals("1")) {

			try {
				ExecutorService executorService = Executors.newFixedThreadPool(5);
				executorService.execute(() -> downloadProfile());
				TimeUnit.MILLISECONDS.sleep(200);
				executorService.execute(() -> downloadURS());
				TimeUnit.MILLISECONDS.sleep(200);
				executorService.execute(() -> downloadQMS());
				TimeUnit.MILLISECONDS.sleep(200);
				executorService.execute(() -> downloadLMS());
				TimeUnit.MILLISECONDS.sleep(200);
				executorService.execute(() -> downloadUR());
				TimeUnit.MILLISECONDS.sleep(200);
				executorService.execute(() -> downloadGroup());
				TimeUnit.MILLISECONDS.sleep(200);

				// Waiting up to 300 seconds for all pending tasks to end
				executorService.shutdown();
				executorService.awaitTermination(300, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				logger.info("Exception in ConnectToCareApp- run method : " + ExceptionUtils.getStackTrace(e) + ","
						+ e.toString());
				e.printStackTrace();
			}
		}

		if (mode.equals("2")) {

		}
	}

	public void downloadProfile() {
		logger.info("User Profile Download Started. Please Wait....");
		System.out.println("User Profile Download Started. Please Wait....");

		UserProfileLoader upl = new UserProfileLoader(rootDirLocation, url, apiVersion, session, properties);
		boolean status = upl.downloadUserProfile(logger);

		if (status) {
			logger.info("User Profile Download Completed");
			System.out.println("User Profile Download Completed");
		} else {
			logger.info("Error in User Profile Download");
			System.out.println("Error in User Profile Download");
		}
	}

	public void downloadURS() {
		
	}

	public void downloadQMS() {
		
	}

	public void downloadLMS() {
	
	}

	public void downloadUR() {
	
	}

	public void downloadGroup() {
		
	}



}
