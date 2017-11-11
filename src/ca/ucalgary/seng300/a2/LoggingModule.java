package ca.ucalgary.seng300.a2;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class LoggingModule {
	private static PrintWriter writer;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	
	
	/* Reference material
	 * https://howtodoinjava.com/core-java/io/how-to-create-a-new-file-in-java/
	 * https://stackoverflow.com/questions/11496700/how-to-use-printwriter-and-file-classes-in-java
	 * https://stackoverflow.com/questions/2885173/how-do-i-create-a-file-and-write-to-it-in-java
	 */
	
	
	/*
	 * writes the log to the apropirate log file by day 
	 */
	public void printToFile(String messageToLog) throws IOException {
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
