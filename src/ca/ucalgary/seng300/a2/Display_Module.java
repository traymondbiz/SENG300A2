
package ca.ucalgary.seng300.a2;

import java.util.Vector;


/**
 * Creates a thread that infinitely alternates display message until interrupt
 * is received. Interrupt is triggered when at least one valid coin is inserted.
 * Once a purchase is made and credits return to zero, the thread 
 */

public class Display_Module  implements Runnable {
	private static Display_Module DisplayM;
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
	
	public void add_loopMessage (String Str, int time) {
		
		  TimeMessage TM = new TimeMessage( Str, time);
		  System.out.println(TM.message);  //Replace with vm.getDisplay().display("Credit: " + Integer.toString(credit));
		  System.out.println(TM.time);  //Replace with vm.getDisplay().display("Credit: " + Integer.toString(credit));
		     
		messageList.addElement(TM);
		
	}
	public void add_message(String str) {
		
		vmngr.Display_Message(str);
	}
	
	
	public void clearList( ) {
		
		messageList.clear();
		
	}
	
	
	private Display_Module(VendingManager host){		
		vmngr = host;
	}
	
	public static void initialize(VendingManager host){
		DisplayM = new Display_Module(host);
	}
	
	public static Display_Module getInstance(){
		return DisplayM;
	}
	
	@Override
	public void run(){
		try{ 
			
			while(!Thread.currentThread().isInterrupted()){
				
				if ( !messageList.isEmpty()) {
					
					
					vmngr.Display_Message(messageList.get(messageIndex).message  );
					Thread.sleep( messageList.get(messageIndex).time );					//Replace with time delay indicated in requirements
					
					
					messageIndex++;
				}
				if ( messageIndex >= messageList.size()) messageIndex =0;
				
			}
			
			messageIndex = 0;
			}catch(InterruptedException e){
				Thread.currentThread().interrupt();
				return;
			}
	}
}
