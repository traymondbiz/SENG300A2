package ca.ucalgary.seng300.a2.test;
 
import static org.junit.Assert.*;
 
import java.util.List;
import java.util.ArrayList;
 
import org.junit.*;
import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;
import ca.ucalgary.seng300.a2.*;
 
public class TestCases {
    private VendingMachine vend;
//  private VendingManager vm;
 
    @Before
    /**
     * Set up the initial values for the vending machine and its elements.
     */
    public void setup() {
        int[] coinKind = {5, 10, 25, 100, 200};
        int selectionButtonCount = 6;
        int coinRackCapacity = 200;     // probably a temporary value
        int popCanRackCapacity = 10;
        int receptacleCapacity = 200; 
        int deliveryChuteCapacity = 5;
        int coinReturnCapacity = 5;
        vend = new VendingMachine(coinKind, selectionButtonCount, coinRackCapacity, popCanRackCapacity, receptacleCapacity, deliveryChuteCapacity, coinReturnCapacity);
         
 //     VendingManager.initialize(vend);
  //    vm = VendingManager.getInstance();
         
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
            popCanCosts.add(200);
        }
        vend.configure(popCanNames, popCanCosts);
    }
     
    @Test
    public void testLoopingThread() throws InterruptedException{
        VendingManager.initialize(vend);
        Thread.sleep(1000);
        assertEquals(VendingListener.returnMsg(), "Hi there!");
    }
     
    @Test
    public void testLoopingThread2() throws InterruptedException{
        VendingManager.initialize(vend);
        Thread.sleep(6000);
        assertEquals(VendingListener.returnMsg(), "");
    }
     
    @Test
    public void testLoopingThread3() throws InterruptedException{
        VendingManager.initialize(vend);
        Thread.sleep(11000);
        assertEquals(VendingListener.returnMsg(), "Hi there!");
    }
     
    @Test
    public void testLoopingTException() {
        VendingManager.initialize(vend);
        VendingManager vm = VendingManager.getInstance();
        try{
            Thread.sleep(10000);
            vm.addCredit(25);
            assertTrue(true);
        } catch (InterruptedException e) {
            assertTrue(false);
        }
    }
 
    @Test
    public void testPostPCreditZero() throws InterruptedException{
        VendingManager.initialize(vend);
        VendingManager vm = VendingManager.getInstance();
        vm.addCredit(200);
        try {
            vm.buy(0);
            Thread.sleep(1000);
            assertEquals(VendingListener.returnMsg(), "Hi there!");
        } catch (InsufficientFundsException | EmptyException | DisabledException | CapacityExceededException e) {
            assertTrue(false);
        }
    }
     
    @Test
    public void testPostPCreditNotZero(){
        VendingManager.initialize(vend);
        VendingManager vm = VendingManager.getInstance();
        vm.addCredit(250);
        try {
            vm.buy(0);
            assertEquals(VendingListener.returnMsg(), "Credit: 50");
        } catch (InsufficientFundsException | EmptyException | DisabledException | CapacityExceededException e) {
            assertTrue(false);
        }
    }
    
	@Test
	public void testInsufficentFundsException(){
		VendingManager.initialize(vend);
		VendingManager vm = VendingManager.getInstance();
		vm.addCredit(50);
		try {
			vm.buy(0);
			assertTrue(false);
		} catch (InsufficientFundsException | EmptyException | DisabledException | CapacityExceededException e){
			assertTrue(true);
		}
	}
     
    @Test
    public void testCreditChange(){
        VendingManager.initialize(vend);
        VendingManager vm = VendingManager.getInstance();
        vm.addCredit(200);
        assertEquals(VendingListener.returnMsg(), "Credit: 200");
    } 
     
    @After
    /**
     * Sets the tested values to null
     * 
     */
    public void tearDown() {
        vend = null; 
    } 
}
