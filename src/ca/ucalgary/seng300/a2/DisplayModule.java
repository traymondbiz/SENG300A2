package ca.ucalgary.seng300.a2;

import java.util.Vector;

/**
 * Software Engineering 300 - Group Assignment 2
 * DisplayModule.java
 * 
 * Creates a thread that infinitely alternates display message until interrupt
 * is received. Interrupt is triggered when at least one valid coin is inserted.
 * Once a purchase is made and credits return to zero, the thread 
 * 
 * Id Input/Output Technology and Solutions (Group 2)
 * @author Raymond Tran 			(30028473)
 * @author Hooman Khosravi 			(30044760)
 * @author Christopher Smith 		(10140988)
 * @author Mengxi Cheng 			(10151992)
 * @author Zachary Metz 			(30001506)
 * @author Abdul Basit 				(30033896)
 * 
 * @version	2.0
 * @since	2.0
 */


/**

 */

public class DisplayModule  implements Runnable {
	private static DisplayModule DisplayM;
	private static VendingManager vmngr;
	
	private Vector<TimeMessage> messageList = new <TimeMessage>Vector() ;
	private int messageIndex =0;
	
	private class TimeMessage {
		public int time;
		public String message;
		
		public  TimeMessage(String MessageIn, int timeIn ) {
			
			message = MessageIn;
			time = timeIn;
			
		}

	}

	private DisplayModule(VendingManager host){		
		vmngr = host;
	}
	
	public static void initialize(VendingManager host){
		DisplayM = new DisplayModule(host);
	}
	
	public static DisplayModule getInstance(){
		return DisplayM;
	}
	
	public void addLoopMessage (String Str, int time) {
		
		  TimeMessage TM = new TimeMessage( Str, time);
		   
		messageList.addElement(TM);
		
	}
	public void addMessage(String str) {
		
		vmngr.Display_Message(str);
	}
	
	
	public void clearList( ) {
		
		messageList.clear();
		
	}
	

	
	@Override
	public void run(){
		try{ 
			
			while(!Thread.currentThread().isInterrupted()){
				
				if (!messageList.isEmpty()) {
					vmngr.Display_Message(messageList.get(messageIndex).message  );
					Thread.sleep( messageList.get(messageIndex).time );					//Replace with time delay indicated in requirements
					
					messageIndex++;
				}
				if ( messageIndex >= messageList.size()) messageIndex =0;
				
			}
			
			// Message index of 0.
		}catch(InterruptedException e){
				Thread.currentThread().interrupt();
				return;
		}
	}
}
