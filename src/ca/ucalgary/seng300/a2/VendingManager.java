package ca.ucalgary.seng300.a2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

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
	private static VendingMachine vm;
	private static Thread noCreditThread;
	private static PrintWriter writer; 
	private int credit = 0;
	
	/**
	 * Singleton constructor. Initializes and stores the singleton instance
	 * of VendingListener.
	 */
	private VendingManager(){
		VendingListener.initialize(this);
		listener = VendingListener.getInstance();
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
		noCreditThread = new Thread(new LoopingThread(vm));
		mgr.noCreditThread.start();		//Starts the looping display message when vm is turned on (created)
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


	// Accessors used throughout the vending logic classes to get hardware references.
	// Indirect access to the VM is used to simplify the removal of the
	// VM class from the build.  
//vvv=======================ACCESSORS START=======================vvv
	
	public Thread getLoopingThread(){
		return (mgr.noCreditThread);
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

	/**
	 * Adds value to the tracked credit.
	 * @param added The credit to add, in cents.
	 */
	void addCredit(int added){
		if(credit == 0){
			mgr.getLoopingThread().interrupt();
		}
		credit += added;
		System.out.println("Credit: " + credit);  //Replace with vm.getDisplay().display("Credit: " + Integer.toString(credit));
	}
	
	void resetDisplay() {
		mgr.noCreditThread.start();
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
	void buy(int popIndex) throws InsufficientFundsException, EmptyException, 
											DisabledException, CapacityExceededException {
		int cost = getPopKindCost(popIndex);
		if (getCredit() >= cost){
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
	
	/* Reference material
	 * https://howtodoinjava.com/core-java/io/how-to-create-a-new-file-in-java/
	 * https://stackoverflow.com/questions/11496700/how-to-use-printwriter-and-file-classes-in-java
	 * https://stackoverflow.com/questions/2885173/how-do-i-create-a-file-and-write-to-it-in-java
	 */
	
	public void printToFile(String messageToLog) throws IOException {
		Date currentDate = new Date();
		
		//Need to strip the date object to only include the year, month, and day for filename
		File currentFileDir = new File("C:\\" + currentDate.toString() + ".txt");
		
		if(!currentFileDir.isFile()) {
			currentFileDir.createNewFile(); 
		}

		writer = new PrintWriter(currentFileDir);
		writer.println(currentDate.toString() + ": " + messageToLog);
		writer.close();
	}
//^^^======================VENDING LOGIC END=======================^^^
}