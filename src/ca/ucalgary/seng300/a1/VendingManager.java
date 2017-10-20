package ca.ucalgary.seng300.a1;

import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

/**
 * VendingManager is the primary access-point for the logic controlling the
 * VendingMachine hardware. It is associated with VendingListener, which listens
 * for event notifications from the hardware classes.
 * 
 * USAGE: Pass VendingMachine to static method initialize(), then use getInstance()
 * to get the singleton VendingManager object. 
 * 
 * Design notes: Methods intended for use only by logic classes are declared with
 * package-access. 
 *
 * &&&&&&&&COMPLETE DOCUMENTATION&&&&&&&&&&&&&&
 */
public class VendingManager {
	private static VendingManager mgr;
	private static VendingListener listener;
	private static VendingMachine vm;
	private int credit = 0;
	
	/*
	 * Singleton constructor. Initializes and gets the singleton instance
	 * of VendingListener. 
	 */
	private VendingManager(){
		VendingListener.initialize(this);
		listener = VendingListener.getInstance();
	}
	
	/**
	 * Provides access to the singleton instance for package-external classes.
	 * @return The singleton VendingManager instance  
	 */
	public static VendingManager getInstance(){
		return mgr;
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
	}
	
	/*
	 * Registers the previously instantiated listener(s) with the 
	 * appropriate hardware.
	 */
	private void registerListeners(){
		getCoinSlot().register(listener);
		registerButtonListener(listener);
	}
	
	/**
	 * Registers a single listener with each selection button.
	 * @param listener A listener that can handle SelectionButton events.
	 */
	private void registerButtonListener(SelectionButtonListener listener){
		int buttonCount = getNumberOfSelectionButtons();
		for (int i = 0; i< buttonCount; i++){
			getSelectionButton(i).register(listener);;
		}		
	}


	// Accessors used through the logic classes to retrieve the VM hardware.
	// Indirect access to the VM is used to simplify the removal of the
	// VM class from the build.  
//vvv=======================ACCESSORS START=======================vvv
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
	SelectionButton getSelectionButton(int index){
		return vm.getSelectionButton(index);
	}
	CoinSlot getCoinSlot(){
		return vm.getCoinSlot(); 
	}
	CoinReceptacle getCoinReceptacle(){
		return vm.getCoinReceptacle(); 
	}
	CoinReceptacle getStorageBin(){
		return vm.getStorageBin(); 
	}
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
	CoinRack getCoinKindForCoinRack(int index){
		return vm.getCoinRackForCoinKind(index); 
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

	/**
	 * Returns the index of the given SelectionButton,
	 * which implies the index of the associated PopRack.
	 * @param button The button of interest.
	 * @return The matching index, or -1 if no match.
	 */
	int getButtonIndex(SelectionButton button){
		int buttonCount = getNumberOfSelectionButtons();
		for (int i = 0; i< buttonCount; i++){
			if (getSelectionButton(i) == button){
				return i;
			}
		}	
		return -1;
	}
	
	/**
	 * Gets the credit stored in the machine, in cents. 
	 * @return The stored credit, in cents.
	 */
	int getCredit(){
		return credit;
	}

	/**
	 * Allows listeners to add value to the tracked credit.
	 * @param added The credit to add, in cents.
	 */
	void addCredit(int added){
		credit += added;
	}
//vvv=======================ACCESSORS END=======================vvv
	

//vvv=======================VENDING LOGIC START=======================vvv	
	/**
	 * Handles pop purchases. Checks if the pop rack has pop, confirms funds available,  
	 *  dispenses the pop, reduces available funds and deposits the added coins into storage. 
	 * @param popIndex The index of the selected pop rack. 	 
	 * @throws InsufficientFundsException Thrown if credit < cost.
	 * @throws EmptyException Thrown if the selected pop rack is empty.
	 * @throws DisabledException Thrown if the pop rack or delivery chute is disabled.
	 * @throws CapacityExceededException Thrown if the delivery chute is full.
	 */
	void buy(int popIndex) throws InsufficientFundsException, EmptyException, 
											DisabledException, CapacityExceededException {
		int cost = getPopKindCost(popIndex);
		if (getCredit() < cost){
			PopCanRack rack = getPopCanRack(popIndex);
			int canCount = rack.size(); //Bad method name; returns # of cans stored
			if (canCount > 0){
				rack.dispensePopCan(); 
				credit -= cost; //Will only be performed if the pop is successfully dispensed.
				getCoinReceptacle().storeCoins(); 
			}
		}
		else {
			int dif = cost - credit;  
			String popName = getPopKindName(popIndex);
			throw new InsufficientFundsException("Cannot buy " + popName + ". " + dif + " cents missing.");
		}
	}
}
