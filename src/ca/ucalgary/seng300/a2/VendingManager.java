package ca.ucalgary.seng300.a2;

import org.lsmr.vending.hardware.*;

/**
 * VendingManager is the primary access-point for the logic controlling the
 * VendingMachine hardware. It is associated with VendingListener, which listens
 * for event notifications from the hardware classes.
 * 
 * USAGE: Pass VendingMachine to static method initialize(), then use getInstance()
 * to get the singleton VendingManager object. Listeners are registered automatically. 
 * 
 * DESIGN: All logic classes are designed as singletons. Currently, the only public-access methods are for initialization
 * and to get a VendingManager instance. All other functionality is restricted
 * to package access. 
 * 
 * TESTING: Due to the near-total encapsulation, VendingManager and VendingListener
 * must be tested along with a VendingMachine. Although a "stub" VendingMachine
 * *could* be created, doing so would be extremely inefficient. 
 * We have been instructed that the VendingMachine and other hardware classes
 * are known-good, so integration testing will be sufficient.
 * 
 * @author Raymond Tran (30028473)
 * @author Thomas Coderre (10169277)
 * @author Thobthai Chulpongsatorn (30005238)
 * 
 */
public class VendingManager {
	private static VendingManager mgr;
	private static VendingListener listener;
	private static ChangeModule changeModule;
	private static VendingMachine vm;
	private static Display_Module DisplayM;
	private static Thread noCreditThread2;
	private int credit = 0;
	
	/**
	 * Singleton constructor. Initializes and stores the singleton instance
	 * of VendingListener.
	 */
	private VendingManager(){
		VendingListener.initialize(this);
		ChangeModule.initialize(this);
		listener = VendingListener.getInstance();
		changeModule = ChangeModule.getInstance();
	}
	
	/**
	 * Replaces the existing singleton instances (if any) for the entire 
	 * the Vending logic package. Registers the VendingListener(s) with the
	 * appropriate hardware.
	 * @param host The VendingMachine which the VendingManager is intended to manage.
	 */
	public static void initialize(VendingMachine host){
		mgr = new VendingManager(); 
		vm = host;
		mgr.registerListeners();
		
		Display_Module.initialize(mgr);
		noCreditThread2 = new Thread(Display_Module.getInstance());
		
		DisplayM =Display_Module.getInstance();
	
		DisplayM.add_loopMessage(new TimeMessage("Hi there!&",5000) );
		DisplayM.add_loopMessage(new TimeMessage("",10000) );
		
		
		//noCreditThread.start();		//Starts the looping display message when vm is turned on (created)
		noCreditThread2.start();
		
		mgr.setModule();			// Sets instance of ChangeModule's validCoins, coinCount, and popPrices arrays.
	}
	
	
	/**
	 * Provides public access to the VendingManager singleton.
	 * @return The singleton VendingManager instance  
	 */
	public static VendingManager getInstance(){
		return mgr;
	}
	
	/*
	 * Registers the previously instantiated listener(s) with the 
	 * appropriate hardware.
	 */
	private void registerListeners(){
		getCoinSlot().register(listener);
		getDisplay().register(listener);
		registerButtonListener(listener);
	}
	
	/**
	 * Iterates through all selection buttons in the VendingMachine and
	 * registers a single listener with each.
	 * @param listener The listener that will handle SelectionButton events.
	 */
	private void registerButtonListener(PushButtonListener listener){
		int buttonCount = getNumberOfSelectionButtons();
		for (int i = 0; i< buttonCount; i++){
			getSelectionButton(i).register(listener);;
		}		
	}
	
	private void setModule() {
		
		int i = vm.getNumberOfCoinRacks();
		int[] inValidCoins = new int[i];
		for (int x = 0; x < i; x++) {
			inValidCoins[x] = vm.getCoinKindForCoinRack(x);
		}
		
		int j = vm.getNumberOfCoinRacks();
		int[] inCoinCount = new int[j];
		for (int x = 0; x < j; x++) {
			CoinRack tempRack = vm.getCoinRack(x);
			inCoinCount[x] = tempRack.size();
		}
		
		int k = vm.getNumberOfPopCanRacks();
		int[] inPopPrices = new int[k];
		for (int x = 0; x < k; x++) {
			inPopPrices[x] = vm.getPopKindCost(x);
		}
		
		changeModule.setCoins(inValidCoins, inCoinCount);
		changeModule.setPopPrices(inPopPrices);
		
	}


	// Accessors used throughout the vending logic classes to get hardware references.
	// Indirect access to the VM is used to simplify the removal of the
	// VM class from the build.  
//vvv=======================ACCESSORS START=======================vvv
	

	public Thread getLoopingThread2(){
		return (noCreditThread2);
	}
	void enableSafety(){
		vm.enableSafety();
	}
	void disableSafety(){
		vm.disableSafety();
	}
	boolean isSafetyEnabled(){
		return vm.isSafetyEnabled();
	}
	IndicatorLight getExactChangeLight(){
		return vm.getExactChangeLight();
	}
	IndicatorLight getOutOfOrderLight(){
		return vm.getOutOfOrderLight();
	}
	int getNumberOfSelectionButtons(){
		return vm.getNumberOfSelectionButtons();
	}
	PushButton getSelectionButton(int index){
		return vm.getSelectionButton(index);
	}
	CoinSlot getCoinSlot(){
		return vm.getCoinSlot(); 
	}
	CoinReceptacle getCoinReceptacle(){
		return vm.getCoinReceptacle(); 
	}
	
	// Deprecated
	//CoinReceptacle getStorageBin(){
	//	return vm.getStorageBin(); 
	//}
	
	DeliveryChute getDeliveryChute(){
		return vm.getDeliveryChute(); 
	}
	int getNumberOfCoinRacks(){
		return vm.getNumberOfCoinRacks();
	}
	CoinRack getCoinRack(int index){
		return vm.getCoinRack(index); 
	}
	CoinRack getCoinRackForCoinKind(int value){
		return vm.getCoinRackForCoinKind(value); 
	}
	Integer getCoinKindForCoinRack(int index){
		return vm.getCoinKindForCoinRack(index); 
	}
	int getNumberOfPopCanRacks(){
		return vm.getNumberOfPopCanRacks(); 
	}
	String getPopKindName(int index){
		return vm.getPopKindName(index); 
	}
	int getPopKindCost(int index){
		return vm.getPopKindCost(index); 
	}
	PopCanRack getPopCanRack(int index){
		return vm.getPopCanRack(index); 
	}
	Display getDisplay(){
		return vm.getDisplay();
	}
	
	CoinReturn getCoinReturn() {
		return vm.getCoinReturn();
	}

	/**
	 * Returns the index of the given SelectionButton,
	 * which implies the index of the associated PopRack.
	 * @param button The button of interest.
	 * @return The matching index, or -1 if no match.
	 */
	int getButtonIndex(PushButton button){
		int buttonCount = getNumberOfSelectionButtons();
		for (int i = 0; i< buttonCount; i++){
			if (getSelectionButton(i) == button){
				return i;
			}
		}	
		return -1;
	}
	
	/**
	 * Gets the credit available for purchases, in cents. 
	 * Public access for testing and external access. 
	 * It is assumed to not be a security vulnerability.
	 * @return The stored credit, in cents.
	 */
	public int getCredit(){
		return credit;
	}
	
	public void Display_Message(String str){
		vm.getDisplay().display(str);
		
	}

    /**
     * Adds value to the tracked credit.
     * @param added The credit to add, in cents.
     */
    public void addCredit(int added){
//      if(credit == 0){
//          mgr.getLoopingThread().interrupt();
//      }
        credit += added;
        System.out.println(credit);     // For debugging
        if(credit != 0) {
            mgr.getLoopingThread2().interrupt();
            
            DisplayM.add_message("Credit: " + Integer.toString(credit));


            System.out.println("Credit: " + credit);  //Replace with vm.getDisplay().display("Credit: " + Integer.toString(credit));
        } 
        else {
        	resetDisplay();
        }
    }
	
	void resetDisplay() {
        noCreditThread2 = new Thread(Display_Module.getInstance());
        noCreditThread2.start();     //Starts the looping display message when vm is turned on (created)
	}
	
	
//^^^=======================ACCESSORS END=======================^^^
	

//vvv=======================VENDING LOGIC START=======================vvv	
	/**
	 * Handles a pop purchase. Checks if the pop rack has pop, confirms funds available,  
	 *  dispenses the pop, reduces available funds and deposits the added coins into storage. 
	 * @param popIndex The index of the selected pop rack. 	 
	 * @throws InsufficientFundsException Thrown if credit < cost.
	 * @throws EmptyException Thrown if the selected pop rack is empty.
	 * @throws DisabledException Thrown if the pop rack or delivery chute is disabled.
	 * @throws CapacityExceededException Thrown if the delivery chute is full.
	 */
	public void buy(int popIndex) throws InsufficientFundsException, EmptyException, 
											DisabledException, CapacityExceededException {
		int cost = getPopKindCost(popIndex);
		if (getCredit() >= cost){
			PopCanRack rack = getPopCanRack(popIndex);
			int canCount = rack.size(); //Bad method name; returns # of cans stored
			if (canCount > 0){
				rack.dispensePopCan(); 
				credit -= cost; //Will only be performed if the pop is successfully dispensed.

				getCoinReceptacle().storeCoins(); 
				System.out.println(credit);		// For debugging
				addCredit(0);
			}
		}
		else {
			int dif = cost - credit;  
			String popName = getPopKindName(popIndex);
			throw new InsufficientFundsException("Cannot buy " + popName + ". " + dif + " cents missing.");
		}
	}

//^^^======================VENDING LOGIC END=======================^^^
}
