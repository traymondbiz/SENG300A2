package ca.ucalgary.seng300.a1;

import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;
import ca.ucalgary.seng300.a1.VendingManager;

public class VendingListener implements CoinSlotListener, SelectionButtonListener {
	static VendingListener listener = new VendingListener();
	VendingManager mgr;
	
	private VendingListener (){}
	
	
	
	protected static VendingListener getListener(){
		return listener;
	}
	
	protected void registerManager(VendingManager manager){
		mgr = manager;
	}
	
	

	// Currently unneeded listener events.
		@Override
		public void coinRejected(CoinSlot slot, Coin coin) {}
		@Override
		public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}
		@Override
		public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}


	/**
	 * Responds to "pressed" notifications from registered SelectionButtons. 
	 */
	@Override
	public void pressed(SelectionButton button) {
		String bName = ""; 
		int bIndex = mgr.getButtonIndex(button); 
		if (bIndex == -1){
			//Then it's not a pop selection button. 
			//This may be where we handle "change return" button presses
		}
		else{
			try{
				//Assumes a 1-to-1, strictly ordered mapping between popIndex and and butttonindex
				mgr.buy(bIndex); 
			} catch(InsufficientFundsException e){
				//TODO Respond to insufficient funds by printing message to display.
				// Should use e.toString().
			} catch(DisabledException e){
				//TODO Respond to the system being disabled.
			} catch (EmptyException e){
				//TODO Respond to the pop rack being empty
			} catch (CapacityExceededException e){
				//TODO Respond to the delivery chute being full.
			}
		}		
	}

	/**
	 * Responds to "Valid coin inserted" notifications from the registered CoinSlot.
	 * Adds the 
	 */
	@Override
	public void validCoinInserted(CoinSlot slot, Coin coin) {
		mgr.addCredit(coin.getValue());
	}
}
