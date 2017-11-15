package ca.ucalgary.seng300.a2;

import java.io.IOException;
import java.util.ArrayList;

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
	private static LoggingModule logger;
	private static VendingMachine vm;
	private static Display_Module DisplayM;
	private static TransactionModule TransactionM;
	private static Thread noCreditThread2;
	private int credit = 0;
	
	/**
	 * Singleton constructor. Initializes and stores the singleton instance
	 * of VendingListener.
	 */
	private VendingManager(){
		VendingListener.initialize(this);
		ChangeModule.initialize(this);
		Display_Module.initialize(this);
		TransactionModule.initialize(this);
		listener = VendingListener.getInstance();
		changeModule = ChangeModule.getInstance();
		TransactionM = TransactionModule.getInstance();
		DisplayM =Display_Module.getInstance();
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
		logger = LoggingModule.getInstance();
		mgr.registerListeners();
		
		
		
		noCreditThread2 = new Thread(Display_Module.getInstance());
		
		
	
		DisplayM.add_loopMessage("Hi there!",5000) ;
		DisplayM.add_loopMessage("",10000) ;
		
		
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
	public int[] getValidCoinsArray() {
		int i = vm.getNumberOfCoinRacks();
		int[] inValidCoins = new int[i];
		for (int x = 0; x < i; x++) {
			inValidCoins[x] = vm.getCoinKindForCoinRack(x);
		}
		return inValidCoins;
	}
	public int[] getCount() {
		int j = vm.getNumberOfCoinRacks();
		int[] inCoinCount = new int[j];
		for (int x = 0; x < j; x++) {
			CoinRack tempRack = vm.getCoinRack(x);
			inCoinCount[x] = tempRack.size();
		}
		return inCoinCount;
		
	}
	public int[] getPopPrices() {
		int k = vm.getNumberOfPopCanRacks();
		int[] inPopPrices = new int[k];
		for (int x = 0; x < k; x++) {
			inPopPrices[x] = vm.getPopKindCost(x);
		}
		return inPopPrices;
	}
	private void setModule() {

		changeModule.setCoins(getValidCoinsArray(), getCount());
		changeModule.setPopPrices(getPopPrices());
		
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
	int getPopCanRackSize(int index){
		return vm.getPopCanRack(index).size(); 
	}
	void dispensePopCanRack(int index) throws DisabledException, EmptyException, CapacityExceededException {
		getPopCanRack(index).dispensePopCan();
	
	}
	void storeCoinsInStorage() throws CapacityExceededException, DisabledException {
		getCoinReceptacle().storeCoins(); 
		
	}
	Display getDisplay(){
		return vm.getDisplay();
	}
	void ReduceCredit(int cost) {
		
		credit -=cost;
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
	public void setCredit(int temp){
		credit = temp;
	}
	
	public void display_Message(String str){
		vm.getDisplay().display(str);
		
	}
	public void add_message(String str) {
		DisplayM.add_message(str);
	}

    /**
     * Adds value to the tracked credit.
     * @param added The credit to add, in cents.
     */

	
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
    public void addCredit(int added){
    	TransactionM.addCredit(added);
    	
    }
    
	
	
	public void buy(int popIndex) throws InsufficientFundsException, EmptyException, 
											DisabledException, CapacityExceededException {
		TransactionM.buy(popIndex);
	}
	
	public void addLog(String msg) {
		try {
			logger.logMessage(msg);
		}catch(IOException e){
			mgr.setOutOfOrder();
		}
		
	}
//^^^======================VENDING LOGIC END=======================^^^
	/**
	 * wrapper method for change module so other modules can interact with it
	 */
	public ArrayList<Integer> getCoinsToReturn(int remaining) {
		return changeModule.getCoinsToReturn(remaining, getValidCoinsArray(), getCount());
	}
	/**
	 * This method will dispense a coin that you specify
	 */
	public void dispenseCoin(int value) {
		try {
		CoinRack temp = getCoinRackForCoinKind(value);
		temp.releaseCoin();
		}catch(CapacityExceededException e) {
			mgr.setOutOfOrder(); //capacity execed cannot recover 
		}catch(EmptyException e) {
			//can recover from here since it tried to release no coins
		}catch(DisabledException e) {
			mgr.setOutOfOrder();
		}
	}
	/**
	 * Call this method to update the state of the exact change light and does
	 * not return anything
	 */
	public void acitvateExactChangeLight() {
		getExactChangeLight().activate();
	}
	public void deactivateExactChangeLight() {
		getExactChangeLight().deactivate();
	}
	public void updateExactChangeLightState() {
		changeModule.updateExactChangeLigthState();
	}
	public void setOutOfOrder() {
		getOutOfOrderLight().activate();
	}
}
