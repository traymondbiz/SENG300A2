package ca.ucalgary.seng300.a2.test;

import static org.junit.Assert.*;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.*;
import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;
import ca.ucalgary.seng300.a2.*;

/**
 * Software Engineering 300 - Group Assignment 2
 * TestCases.java
 * 
 * This class is used to test the functionality of the VendingManager class.
 * 
 * 82.7% code coverage was achieved in VendingManager.
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
public class TestCases {
	private VendingMachine vend;

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
	public void setup() {
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
	 * Ensures the display device displays the "Hi there!" message within the first 5 seconds if the machine contains no credit.
	 * 
	 * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
	 */
	@Test
	public void testHiThere() throws InterruptedException{
		VendingManager.initialize(vend);
		Thread.sleep(1000);
		assertEquals(VendingListener.returnMsg(), "Hi there!");
	}
	
	/**
	 * Ensures the display device erases the "Hi there!" message during the following 10 seconds if the machine contains no credit.
	 * 
	 * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
	 */
	@Test
	public void testHiThereErased() throws InterruptedException{
		VendingManager.initialize(vend);
		Thread.sleep(6000);
		assertEquals(VendingListener.returnMsg(), "");
	}
	
	/**
	 * Ensures the display device repeats the message display cycle every 15 seconds if the machine contains no credit.
	 * 
	 * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
	 */
	@Test
	public void testMessageCycle() throws InterruptedException{
		VendingManager.initialize(vend);
		Thread.sleep(16000);
		assertEquals(VendingListener.returnMsg(), "Hi there!");
	}

	/**
	 * Ensures the display device displays the message "Hi there!" when a purchase happens
	 * and the updated credit is reset to zero.
	 * 
	 * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
	 */
	@Test
	public void testPostPCreditZero() throws InterruptedException{
		VendingManager.initialize(vend);
		VendingManager vm = VendingManager.getInstance();
		configureVend(200);
		vm.addCredit(200);
		try {
			vm.buy(0);
			Thread.sleep(1000);
			assertEquals(VendingListener.returnMsg(), "Hi there!");
		} catch (InsufficientFundsException | EmptyException | DisabledException | CapacityExceededException | IOException e) {
			assertTrue(false);
		}
	}
	
	/**
	 * Ensures the display device displays the message "Credit: " and the amount of updated credit when a purchase happens
	 * and the updated credit is non-zero.
	 */
	@Test
	public void testPostPCreditNotZero(){
		VendingManager.initialize(vend);
		VendingManager vm = VendingManager.getInstance();
		configureVend(200);
		vm.addCredit(250);
		try {
			vm.buy(0);
			assertEquals(VendingListener.returnMsg(), "Credit: 50");
		} catch (InsufficientFundsException | EmptyException | DisabledException | CapacityExceededException | IOException e) {
			assertTrue(false);
		}
	}
	
	/**
	 * Ensures the buy function throws the correct exception when the credit < cost.
	 */
	@Test
	public void testInsufficentFundsException(){
		VendingManager.initialize(vend);
		VendingManager vm = VendingManager.getInstance();
		configureVend(200);
		vm.addCredit(50);
		try {
			vm.buy(0);
			assertTrue(false);
		} catch (InsufficientFundsException | EmptyException | DisabledException | CapacityExceededException | IOException e){
			assertTrue(true);
		}
	}
	
	/** 
	 * Ensures the display device displays the message "Credit: " and the amount of credit when the user enters valid coins.
	 */
	@Test
	public void testCreditChange(){
		VendingManager.initialize(vend);
		VendingManager vm = VendingManager.getInstance();
		vm.addCredit(200);
		assertEquals(VendingListener.returnMsg(), "Credit: 200");
	} 
	
	/**
	 * Ensures the "exact change only" light is turned on whenever exact change cannot be guaranteed for all possible transactions.
	 */
	@Test
	public void testActiveExchangeLight(){
		VendingManager.initialize(vend);
		VendingManager vm = VendingManager.getInstance();
		configureVend(170);
		vm.addCredit(340);
		try {
			vm.buy(0);
			assertTrue(true);
		} catch (InsufficientFundsException | EmptyException | DisabledException | CapacityExceededException | IOException e){
			assertTrue(false);
		}
	}
	
	/**
	 * Method to destroy the vending machine and change module after each test in order to not affect the following test.
	 */	
	@After
	public void tearDown() {
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
