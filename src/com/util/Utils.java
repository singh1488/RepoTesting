package com.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class Utils {

	public static Properties readPropertiesFile(String rootDir) throws IOException {
		Properties propertiesFile = null;

		String filePath = rootDir + File.separator + "config.properties";

		if (filePath != null) {
			FileReader reader = new FileReader(filePath);
			propertiesFile = new Properties();
			propertiesFile.load(reader);
		}
		
		return propertiesFile;
	}
	
	public static String getApiUrl(Properties properties){
		return properties.getProperty("apiUrl");
	}
	
	public static String getApiVersion(Properties properties){
		return properties.getProperty("apiVersion");
	}
	
	public static String getUserName(Properties properties){
		return properties.getProperty("userName");
	}
	
	public static String getPassword(Properties properties){
		return properties.getProperty("password");
	}
	
	public static String getLoaderDirName(Properties properties){
		return properties.getProperty("loaderDirName");
	}
	
	public static String getUserProfileLoaderFileName(Properties properties){
		return properties.getProperty("userProfileLoaderFileName");
	}
	
	public static String getUserRoleSetupLoaderFileName(Properties properties){
		return properties.getProperty("userRoleSetupLoaderFileName");
	}
	
	public static String getUserRoleLoaderFileName(Properties properties){
		return properties.getProperty("userRoleLoaderFileName");
	}
	
	public static String getQMSUserRoleSetupLoaderFileName(Properties properties){
		return properties.getProperty("QMSUserRoleSetupLoaderFileName");
	}
	
	public static String getLMSUserRoleSetupLoaderFileName(Properties properties){
		return properties.getProperty("LMSUserRoleSetupLoaderFileName");
	}
	
	public static String getUserGroupsLoaderFileName(Properties properties){
		return properties.getProperty("userGroupsLoaderFileName");
	}
	
	public static String getConnectToCareMappingFilePath(Properties properties){
		return properties.getProperty("connectToCareMappingFilePath");
	}
	
	public static String getApplicationRoleMappingFilePath(Properties properties){
		return properties.getProperty("applicationRoleMappingFilePath");
	}
	
	
	public static boolean isFileExists(String sFilePath) {
		File oFile = new File(sFilePath);
		return oFile.exists();
	}
	
	public static <T> Set<T> convertToSet(List<T> list) {
	    return list.stream().collect(Collectors.toCollection(LinkedHashSet::new));
	}
}
