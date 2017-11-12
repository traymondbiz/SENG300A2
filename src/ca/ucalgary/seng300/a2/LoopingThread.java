package ca.ucalgary.seng300.a2;

import org.lsmr.vending.hardware.VendingMachine;

/**
 * Software Engineering 300 - Group Assignment 2
 * LoopingThread.java
 * 
 * Creates a thread that infinitely alternates display message until interrupt
 * is received. Interrupt is triggered when at least one valid coin is inserted.
 * Once a purchase is made and credits return to zero, the thread restarts the 
 * message displaying cycle.
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
public class LoopingThread implements Runnable {
	private static LoopingThread loopingT;
	private static VendingMachine vm;
	
	/**
	 * Singleton constructor. Stores the singleton instance of LoopingThread.
	 */
	private LoopingThread(VendingMachine host){		
		vm = host;
	}
	
	/**
	 * Creates a new LoopingThread instance.
	 * @param host The VendingMachine which the LoopingThread is intended to manage.
	 */
	public static void initialize(VendingMachine host){
		loopingT = new LoopingThread(host);
	}
	
	/**
	 * Provides public access to the LoopingThread singleton.
	 * @return The singleton LoopingThread instance  
	 */
	public static LoopingThread getInstance(){
		return loopingT;
	}
	
	/**
	 * Thread.start() causes the object's run method to be called in that separately executing thread.
	 */
	@Override
	public void run(){
		try{
			while (!Thread.currentThread().isInterrupted()){
				vm.getDisplay().display("Hi there!");
				Thread.sleep(5000);
				vm.getDisplay().display("");
				Thread.sleep(10000);
				} 
			}catch(InterruptedException e){
				Thread.currentThread().interrupt();
				return;
			}
	}
}
