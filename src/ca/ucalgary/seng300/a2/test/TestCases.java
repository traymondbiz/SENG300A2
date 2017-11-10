package ca.ucalgary.seng300.a2.test;

import static org.junit.Assert.*;
import org.junit.*;
import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;
import ca.ucalgary.seng300.a2.*;

public class TestCases {
	private VendingMachine vend;
	private TheDisplayListener dListen;
	private TheCoinSlotListener csListen;
	
	@Before
	/**
	 * Set up the initial values for the vending machine and its elements.
	 */
	public void setup() {
    	int[] coinKind = {5, 10, 25, 100, 200};
    	int selectionButtonCount = 6;
    	int coinRackCapacity = 200;		// probably a temporary value
    	int popCanRackCapacity = 10;
    	int receptacleCapacity = 200; 
    	vend = new VendingMachine(coinKind, selectionButtonCount, coinRackCapacity, popCanRackCapacity, receptacleCapacity);
    	
    	dListen = new TheDisplayListener();
		(vend.getDisplay()).register(dListen);
		
		csListen = new TheCoinSlotListener(vend);
		(vend.getCoinSlot()).register(csListen);
	}
	
//	@Test
//	public void testLoopingThread(){
//		VendingManager.initialize(vend);
//		assertEquals(vend.getDisplay(), "Hi there!");
//	}
	
//	@Test
//	public void tesLoopingThread() throws InterruptedException{
//		vm.initialize(vend);
//		vm.wait(1000);
//		assertEquals(vend.getDisplay(), "          ");
//	}

	@Test
	public void testAfterCoinInserted(){
		Coin coin = new Coin(5);
		TheCoinSlotListener csListen = new TheCoinSlotListener(vend);
		csListen.validCoinInserted(vend.getCoinSlot(), coin);
		assertEquals(dListen.returnMsg(), "Credit: " + coin);	
	} 
	
	@After
	/**
	 * Sets the tested values to null
	 * 
	 */
	public void tearDown() {
		vend = null; 
		dListen = null;
		csListen = null;
	} 
}
