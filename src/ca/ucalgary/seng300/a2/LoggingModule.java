package ca.ucalgary.seng300.a2;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.VendingMachine;

public class LoggingModule {
	private static PrintWriter writer;
	private static LoggingModule logger;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	/*
	 * This class
	 */
	
	
	/* Reference material
	 * https://howtodoinjava.com/core-java/io/how-to-create-a-new-file-in-java/
	 * https://stackoverflow.com/questions/11496700/how-to-use-printwriter-and-file-classes-in-java
	 * https://stackoverflow.com/questions/2885173/how-do-i-create-a-file-and-write-to-it-in-java
	 */
	
	
	/*
	 * writes the log to the apropirate log file by day 
	 */
	public static void initialize() {
			logger = new LoggingModule();
			
		}

		/**
		 * Provides access to the singleton instance for package-internal classes.
		 * @return The singleton ChangeModule instance  
		 */
	
	public static LoggingModule getInstance() {
		return logger;
	}
	private void printToFile(String messageToLog) throws IOException {
		Date currentDate = new Date();
		
		//Need to strip the date object to only include the year, month, and day for filename
		File currentFileDir = new File("C:\\" + currentDate.toString() + ".txt");
		
		if(!currentFileDir.isFile()) {
			currentFileDir.createNewFile(); 
		}

		writer = new PrintWriter(currentFileDir);
		writer.println(currentDate.toString() + ": " + messageToLog);
		writer.close();
	}

}
