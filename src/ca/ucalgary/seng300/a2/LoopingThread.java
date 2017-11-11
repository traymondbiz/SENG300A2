package ca.ucalgary.seng300.a2;

import org.lsmr.vending.hardware.VendingMachine;

/**
 * Creates a thread that infinitely alternates display message until interrupt
 * is received. Interrupt is triggered when at least one valid coin is inserted.
 * Once a purchase is made and credits return to zero, the thread 
 */

public class LoopingThread implements Runnable {
	private static VendingMachine vm;
	
	public LoopingThread(VendingMachine host){		
		vm = host;
	}
	
    @Override
    public void run(){
        try{
            while (!Thread.currentThread().isInterrupted()){
                vm.getDisplay().display("Hi there!");   //Replace with vm.getDisplay().display("Hi there!")
                Thread.sleep(5000);                 //Replace with time delay indicated in requirements
                vm.getDisplay().display("");    //Replace with vm.getDisplay().display("")
                Thread.sleep(10000);
                } 
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
                return;
            }
    }
}
