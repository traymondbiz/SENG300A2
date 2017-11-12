package ca.ucalgary.seng300.a2.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.junit.Assert.*;
import ca.ucalgary.seng300.a2.*;

import org.lsmr.vending.PopCan;
import org.lsmr.vending.hardware.*;

/**
 * Software Engineering 300 - Group Assignment 2
 * ChangeModuleTest.java
 * 
 * This class is used to test the functionality of the ChangeModule class.
 * 
 * 90.4% code coverage was achieved in ChangeModule.
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
public class ChangeModuleTest {	
	private ChangeModule cm;
	private VendingMachine vend;
	private int[] validCoins = {1, 10, 5, 25, 100, 200};
	private int[] coinCount = {0, 0, 1, 3, 0, 0};
	
	/**
	 * A method to prepare a vending machine to the basic specifications outlined by the Client 
	 * Canadian coins
	 * 6 buttons/kinds of pop
	 * 200 coins in each coin rack
	 * 10 pops per rack
	 * 200 coins can be stored in each receptacle
	 * 5 pops per delivery chute
	 * 5 coins can be returned in each receptacle
	 */
	@Before
	public void setup(){
    	int[] coinKind = {1, 5, 10, 25, 100, 200};
    	int selectionButtonCount = 6;
    	int coinRackCapacity = 200;	
    	int popCanRackCapacity = 10;
    	int receptacleCapacity = 200; 
    	int deliveryChuteCapacity = 5;
    	int coinReturnCapacity = 5;
    	vend = new VendingMachine(coinKind, selectionButtonCount, coinRackCapacity, popCanRackCapacity, receptacleCapacity, deliveryChuteCapacity, coinReturnCapacity);
	}
	
	/**
	 * Ensure the getPossibleChangeValues and canMakeChange algorithms calculate correctly when the machine is not able to make exact change.
	 */
	@Test
	public void testNotExactChange(){
		configureVend(170);
		cm = ChangeModule.getInstance();
		boolean expected = cm.checkChangeLight(validCoins, validCoins, coinCount);
		assertEquals(expected, false);
	}
	
	/**
	 * Ensure the getPossibleChangeValues algorithm calculates correctly when the machine is able to make exact change.
	 */
	@Test
	public void testCheckChangeLight2(){
		configureVend(200);
		cm = ChangeModule.getInstance();
		boolean expected = cm.checkChangeLight(validCoins, validCoins, coinCount);
		assertEquals(expected, true);
	}

	/**
	 * Ensure thegetPossibleChangeValues and canMakeChange algorithms calculate correctly when the machine is able to make exact change.
	 */
	@Test
	public void testCheckChangeLight3(){
		configureVend(150);
		cm = ChangeModule.getInstance();
		boolean expected = cm.checkChangeLight(validCoins, validCoins, coinCount);
		assertEquals(expected, true);
	}
	
	/**
	 * Method to destroy the vending machine and change module after each test in order to not affect the following test.
	 */
	@After
	public void tearDown(){
		cm = null;
		vend = null;
	}
	
	/**
     * Configures the hardware to use a set of names and costs for pop cans.
     * 
	 * @param popPrice Cost for each pop. Cannot be non-positive.
	 */
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
		};
		
		List<Integer> popCanCosts = new ArrayList<Integer>();
		for (int i = 0; i < 6; i++) {
			popCanCosts.add(popPrice);
		}
		vend.configure(popCanNames, popCanCosts);	
    	VendingManager.initialize(vend);		
	}
}
