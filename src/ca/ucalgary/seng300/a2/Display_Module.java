
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
	
	private Vector<TimeMessage> messageList;
	private int messageIndex =0;
	
	
	public void add_message ( TimeMessage Input) {
		
		messageList.addElement(Input);
		
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
