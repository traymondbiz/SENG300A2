package ca.ucalgary.seng300.a2;

import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

public class TheCoinSlotListener implements CoinSlotListener {
	private int credit = 0;
	private VendingMachine vend;
	
	public TheCoinSlotListener(VendingMachine vend) {
		this.vend = vend;
	}
	
	
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		// nothing for now
		
	}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		// nothing for now
		 
	}

	@Override
	public void validCoinInserted(CoinSlot slot, Coin coin) {
		credit += coin.getValue();
		vend.getDisplay().display("Credit: " + credit);
	} 

	@Override
	public void coinRejected(CoinSlot slot, Coin coin) {
		// nothing for now
		// probably trigger display message 
	}

	public int getCurrentCredit() {
		return credit;
	}
	
	public void valueCharged(int price) {
		if (price <= credit) {
			credit -= price;
		}
	}
}

