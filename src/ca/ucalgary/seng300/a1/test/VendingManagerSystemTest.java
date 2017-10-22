package ca.ucalgary.seng300.a1.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.vending.Coin;
import org.lsmr.vending.Deliverable;
import org.lsmr.vending.hardware.CapacityExceededException;
import org.lsmr.vending.hardware.CoinSlot;
import org.lsmr.vending.hardware.DeliveryChute;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.EmptyException;
import org.lsmr.vending.hardware.SelectionButton;
import org.lsmr.vending.hardware.VendingMachine;

import ca.ucalgary.seng300.a1.InsufficientFundsException;
import ca.ucalgary.seng300.a1.VendingListener;
import ca.ucalgary.seng300.a1.VendingManager;

public class VendingManagerSystemTest {
	
	private VendingMachine machine = null;
	private VendingManager manager = null;
	
	@Before
	public void setup() throws Exception{
		
		List<String> popCanNames = Arrays.asList("Coke", "Sprite", "Crush", "Ale", "Pepsi", "Diet");
		List<Integer> popCanCosts = Arrays.asList(250,250,250,250,250,250);
		
		
		int[] coinKinds = new int[] {5, 10, 25, 100, 200};
		int selectionButtonCount = 6; 
		int coinRackCapacity = 200;
		int popCanRackCapacity = 15;
		int receptacleCapacity = 200;	
		machine = new VendingMachine(coinKinds, selectionButtonCount, coinRackCapacity, popCanRackCapacity, receptacleCapacity); 
		machine.configure(popCanNames, popCanCosts);
		
		VendingManager.initialize(machine);
		manager = VendingManager.getInstance();
		
		
	}
	
	@Test
	public void testCreditAndPop() throws InsufficientFundsException, EmptyException, DisabledException, CapacityExceededException {
		
		machine.loadPopCans(15,15,15,15,15,15);
		machine.loadCoins(10,10,10,10,10);
		CoinSlot slot = machine.getCoinSlot();
		Coin coin = new Coin(100);
		VendingListener.getInstance().validCoinInserted(slot, coin); 
		VendingListener.getInstance().validCoinInserted(slot, coin); 
		VendingListener.getInstance().validCoinInserted(slot, coin); 
		
		SelectionButton button = machine.getSelectionButton(1);
		VendingListener.getInstance().pressed(button);
		
		DeliveryChute chute = machine.getDeliveryChute();
		Deliverable[] delivered = chute.removeItems();
		
		String expect = new String("Sprite");	
		String pop = delivered[0].toString();
		
		assertEquals(pop, expect);
		assertEquals(manager.getCredit(), 50);
	}

	@Test
	public void testCreditAndNoPop() throws InsufficientFundsException, EmptyException, DisabledException, CapacityExceededException{
		
		machine.loadPopCans(0,0,0,0,0,0);
		machine.loadCoins(10,10,10,10,10);
		CoinSlot slot = machine.getCoinSlot();
		Coin coin = new Coin(100);
		VendingListener.getInstance().validCoinInserted(slot, coin); 
		VendingListener.getInstance().validCoinInserted(slot, coin); 
		VendingListener.getInstance().validCoinInserted(slot, coin); 
	
		SelectionButton button = machine.getSelectionButton(1);
		VendingListener.getInstance().pressed(button);
		
		DeliveryChute chute = machine.getDeliveryChute();
		Deliverable[] delivered = chute.removeItems();
		
		assertEquals(delivered.length, 0);
		assertEquals(manager.getCredit(), 300);
	}
	
	@Test
	public void testNoCreditAndPop() throws InsufficientFundsException, EmptyException, DisabledException, CapacityExceededException{
		
		machine.loadPopCans(15,15,15,15,15,15);
		
		SelectionButton button = machine.getSelectionButton(1);
		VendingListener.getInstance().pressed(button);
		
		DeliveryChute chute = machine.getDeliveryChute();
		Deliverable[] delivered = chute.removeItems();
		
		assertEquals(delivered.length, 0);
		assertEquals(manager.getCredit(), 0);
	}
	
	
	

}
