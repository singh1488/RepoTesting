package com.connecttocare;

import java.util.Scanner;

public class ConnectToCare {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) { // + File.separator + "config.properties"
		
		//BasicConfigurator.configure();
		
		String rootDirLocation = System.getProperty("user.dir");
		
		System.out.println("1 = Extract all loader files\n"
				+ "2 = Genrate Connect to Care mapping report" );
		
		System.out.print("Please, Choose the mode : ");
		
		Scanner scanner = new Scanner(System.in);
		 
        String mode = scanner.nextLine();
		
		System.out.println("Mode : " + mode);
		
		String[] params = new String[]{ rootDirLocation, mode }; 
		
		ConnectToCareApp connectToCareApp = new ConnectToCareApp();	
		connectToCareApp.run(args, params);
		
		
	}
}
