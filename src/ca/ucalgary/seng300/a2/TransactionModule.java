package ca.ucalgary.seng300.a2;

import java.util.ArrayList;

import org.lsmr.vending.hardware.CapacityExceededException;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.EmptyException;


public class TransactionModule {
	private static VendingManager mngr;
	private static TransactionModule TransactionM;
	

	
	public static void initialize(VendingManager host){
		TransactionM = new TransactionModule();
		mngr  = host;
	}
	
	public static TransactionModule getInstance(){
		return TransactionM;
	}
	
	
	
    public void addCredit(int added){
    	mngr.setCredit( added + mngr.getCredit()  );
        
        System.out.println(mngr.getCredit());     // For debugging
        if(mngr.getCredit() != 0) {
            mngr.getLoopingThread2().interrupt();
            
            mngr.add_message("Credit: " + Integer.toString(mngr.getCredit()));
            

        } 
        else {
        	mngr.resetDisplay();
        }
    }
	

	
	public void buy(int popIndex) throws InsufficientFundsException, EmptyException, 
											DisabledException, CapacityExceededException {
		
		int cost = mngr.getPopKindCost(popIndex);
		if (mngr.getCredit() >= cost){
			
			
			int canCount = mngr.getPopCanRackSize(popIndex); //Bad method name; returns # of cans stored
			if (canCount > 0){
				
				
				mngr.dispensePopCanRack(popIndex);
				int remaining = mngr.getCredit() - cost ;
				if(remaining > 0) { // if true there is change to give
					ArrayList<Integer> returnList = mngr.getCoinsToReturn(remaining); // get the remaining
					while(!returnList.isEmpty()) {
						mngr.dispenseCoin(returnList.get(0));
						remaining -= returnList.get(0);
						returnList.remove(0);
					}
				}
				mngr.setCredit(remaining); //all change has been given
				mngr.storeCoinsInStorage();
				mngr.updateExactChangeLightState();
				mngr.addCredit(0); //update screen with adding 0 credit
				if(popIsEmpty()) { // set the out of order light on if we are out of pop in all racks 
					mngr.setOutOfOrder();
				}
			}
		}
		else {
			int dif = cost - mngr.getCredit();  
			String popName = mngr.getPopKindName(popIndex);
			throw new InsufficientFundsException("Cannot buy " + popName + ". " + dif + " cents missing.");
		}
	}
	
	
	private boolean popIsEmpty() {
		for(int i=0;i<mngr.getNumberOfPopCanRacks();i++) {
			if(mngr.getPopCanRack(i).size() > 0) {
				return false;
			}
		}
		return true;
	}
	
	
}
