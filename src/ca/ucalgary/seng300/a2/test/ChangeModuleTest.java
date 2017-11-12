package ca.ucalgary.seng300.a2.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.junit.Assert.*;
import ca.ucalgary.seng300.a2.*;

import org.lsmr.vending.PopCan;
import org.lsmr.vending.hardware.*;


public class ChangeModuleTest {	
	private ChangeModule cm;
	private VendingMachine vend;
	private int[] validCoins = {1, 10, 5, 25, 100, 200};
	private int[] coinCount = {0, 0, 1, 3, 0, 0};
	
	@Before
	public void setup(){
    	int[] coinKind = {1, 5, 10, 25, 100, 200};
    	int selectionButtonCount = 6;
    	int coinRackCapacity = 200;		// probably a temporary value
    	int popCanRackCapacity = 10;
    	int receptacleCapacity = 200; 
    	int deliveryChuteCapacity = 5;
    	int coinReturnCapacity = 5;
    	vend = new VendingMachine(coinKind, selectionButtonCount, coinRackCapacity, popCanRackCapacity, receptacleCapacity, deliveryChuteCapacity, coinReturnCapacity);
	}
	
	@Test
	public void testCheckChangeLight(){
		configureVend(170);
		cm = ChangeModule.getInstance();
		boolean expected = cm.checkChangeLight(validCoins, validCoins, coinCount);
		assertEquals(expected, false);
	}
	
	@Test
	public void testCheckChangeLight2(){
		configureVend(200);
		cm = ChangeModule.getInstance();
		boolean expected = cm.checkChangeLight(validCoins, validCoins, coinCount);
		assertEquals(expected, true);
	}
	
	@Test
	public void testCheckChangeLight3(){
		configureVend(150);
		cm = ChangeModule.getInstance();
		boolean expected = cm.checkChangeLight(validCoins, validCoins, coinCount);
		assertEquals(expected, true);
	}
	
	@After
	public void tearDown(){
		cm = null;
		vend = null;
	}
	
	void configureVend(int popPrice){
		List<String> popCanNames = new ArrayList<String>();
		popCanNames.add("Coke"); 
		popCanNames.add("Pepsi"); 
		popCanNames.add("Sprite"); 
		popCanNames.add("Mountain dew"); 
		popCanNames.add("Water"); 
		popCanNames.add("Iced Tea");
		
		PopCan popcan = new PopCan("Coke");
		try {
			vend.getPopCanRack(0).acceptPopCan(popcan);
		} catch (CapacityExceededException | DisabledException e) {
			e.printStackTrace();
		};
		
		List<Integer> popCanCosts = new ArrayList<Integer>();
		for (int i = 0; i < 6; i++) {
			popCanCosts.add(popPrice);
		}
		vend.configure(popCanNames, popCanCosts);	
    	VendingManager.initialize(vend);		
	}
}
