package ca.ucalgary.seng300.a2;

import java.io.IOException;

import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

/**
 * This class is registered by VendingManager with hardware classes to listen for hardware
 * events and perform first-pass checks and error-handling for them. Most "heavy-lifting" 
 * is completed within VendingManager.
 * 
 * ACCESS: Only listener methods are public access. 
 * 
 * HANDLED EVENTS: 	SelectionButtonListener: pressed() 
 *   				CoinSlotListener: ValidCoinInserted()
 *
 * @author Raymond Tran (30028473)
 * @author Thomas Coderre (10169277)
 * @author Thobthai Chulpongsatorn (30005238)
 *
 */
public class VendingListener implements CoinSlotListener, PushButtonListener, CoinReturnListener, DisplayListener {
	private static VendingListener listener;
	private static VendingManager mgr;
	private static String message = null;
	
	private VendingListener (){}
	
	/**
	 * Forces the existing singleton instance to be replaced.
	 * Called by VendingManager during its instantiation.
	 */
	public static void initialize(VendingManager manager){		
		if (manager != null){
			mgr = manager;
			listener = new VendingListener();
		}
	}
	
	/**
	 * Provides access to the singleton instance for package-internal classes.
	 * @return The singleton VendingListener instance  
	 */
	public static VendingListener getInstance(){
		return listener;
	}

	// Currently unneeded listener events.
	@Override
	public void coinRejected(CoinSlot slot, Coin coin) {}
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}
	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}
	@Override
	public void returnIsFull(CoinReturn coinReturn) {}

	/**
	 * Responds to "pressed" notifications from registered SelectionButtons. 
	 * If no matching button is found in the VendingMachine, nothing is done.
	 * Uses the buy() method in VendingManager to process the purchase.
	 * All exceptions thrown by buy() are caught here (InsufficientFunds, Disabled, Empty, etc.) 
	 * @throws IOException 
	 */
	@Override
	public void pressed(PushButton button) {
		
		int bIndex = mgr.getButtonIndex(button); 
		if (bIndex == -1){
			//Then it's not a pop selection button. 
			//This may be where we handle "change return" button presses
		}
		else{
			try{
				//Assumes a 1-to-1, strictly ordered mapping betwee
				mgr.buy(bIndex); 
				mgr.addLog(mgr.getPopKindName(bIndex)+" button pressed by user with: "+ Integer.toString(mgr.getCredit()));
			} catch(InsufficientFundsException e){
				//TODO Respond to insufficient funds by printing message to display.
				// Should use e.toString().
				mgr.addLog("User Could not purchase " + mgr.getPopKindName(bIndex) + ". " + Integer.toString(mgr.getPopKindCost(bIndex)-mgr.getCredit()) + " cents missing from credit.");
			} catch(DisabledException e){
				//TODO Respond to the system being disabled
				mgr.addLog("User Could not purchase " + mgr.getPopKindName(bIndex) + " since the system is disabled");
			} catch (EmptyException e){
				mgr.addLog("User Could not purchase " + mgr.getPopKindName(bIndex) + " becasue there is none in the machine.");
				//TODO Respond to the pop rack being empty
			} catch (CapacityExceededException e){
				mgr.addLog("User Could not purchase " + mgr.getPopKindName(bIndex) + " becasue the deivery chute is full of change");
				//TODO Respond to the delivery chute being full.
			}
		}		
	}

	/**
	 * Responds to "Valid coin inserted" notifications from the registered CoinSlot.
	 * Adds the value of the coin to the VendingManager's tracked credit.
	 * @throws IOException 
	 */
	@Override
	public void validCoinInserted(CoinSlot slot, Coin coin) {
		mgr.addCredit(coin.getValue());
		
			mgr.addLog("User instered: " + Integer.toString(coin.getValue()) +"coin to coin slot");
		
	}

	@Override
	public void coinsDelivered(CoinReturn coinReturn, Coin[] coins){
		mgr.resetDisplay();
		
		mgr.addLog("Coins Returned to User: " + coins.toString());
		
	}
	
	@Override
	public void messageChange(Display display, String oldMessage, String newMessage) {
		message = newMessage;
	}

	public static String returnMsg(){
		return message;
	}
}
