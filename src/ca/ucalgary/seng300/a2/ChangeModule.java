package ca.ucalgary.seng300.a2;

import java.util.ArrayList;

/* Consider removing and performing purely logic.
 * Note that we have to compensate for the fact that ChangeModule needs a sorted list.
 * (Check commented out QuickSort for more information.)
 * 
 * Elsewhere:
 * i = vm.getNumberOfCoinRacks();
 * list[x] = vm.getCoinKindForCoinRack(x); for x = 0; x < i; x++
 * validCoins = list; // list[x] in VendingManager is sent to ChangeModule
 * 
 * j = vm.getNumberofCoinRacks();
 * tempRack = vm.getCoinRack(x); for x = 0; x < j; x++
 * list[x] = tempRack.size();
 * coinCount = list; // list[x] in VendingManager is sent to ChangeModule
 * 
 * k = vm.getNumberOfPopCanRacks();
 * list[x] = vm.getPopKindCost(x); for x = 0; x < k; x++
 * popPrices = list; // list[x] in VendingManager is sent to ChangeModule
 * 
 * validCoins, coinCount, and popPrices are sent to ChangeModule, removing the hardware reliance.
 * 
 * refreshExactChangeLight returns boolean for true (light needs to be on), or false (light needs to be off)
 * to VendingManager which will perform the actual operation.
 * 
 * Then, merge the two canMakeChange() methods.
 */

//import org.lsmr.vending.hardware.VendingMachine;

/**
 * Software Engineering 300 - Group Assignment 2
 * ChangeModule.java
 * 
 * An algorithm that determines how excess change a given vending machine would
 * potentially have to return. This is output in the form of a list.
 * For example, a list [5, 25, 30, 50] would mean the Vending Machine has to be able
 * to return 5, 25, 30, and 50 cents on a given transaction. If one of these is not
 * possible, the exact change light is turned on.
 * 
 * Furthermore, due to the logic relying on mathematics, any currency-pop combination.
 * is compatible provided that the appropriate lists are sent of ChangeModule to be calculated on.
 * 
 * Contains human-readable print to console statements for debugging purposes.
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
public class ChangeModule {
	
 	/* 
	 * Initialized arrays representing arbitrary values to calculate on.
	 * valid_coins	represents the machine's accepted currency denominations.
	 * pop_prices	represents the costs of some particular pop. (Dependent on index.)
	 * coin_count	represents the current available amount of coins (excluding the user's)
	 * 				that can be used to make change. Ascending value order.
	 */
	private static int[] validCoins = {1, 10, 5, 25, 100, 200};
	private static int[] coinCount 	= {0, 0, 1, 3, 0, 0};
	private static int[] popPrices 	= {125, 150, 170, 200};
	
	private static ChangeModule changeModule;
	private static VendingManager mgr;
	
	private ChangeModule() {}
	
	/**
	 * Forces the existing singleton instance to be replaced.
	 * Called by VendingManager during its instantiation.
	 */
	public static void initialize(VendingManager manager) {
		if (manager != null) {
			mgr = manager;
			changeModule = new ChangeModule();
		}
	}

	/**
	 * Provides access to the singleton instance for package-internal classes.
	 * @return The singleton ChangeModule instance  
	 */
	public static ChangeModule getInstance() {
		return changeModule;
	}
	
	public void setCoins(int[] inValidCoins, int[] inCoinCount) {
		if (inValidCoins.length == inCoinCount.length) {
			validCoins = inValidCoins;
			coinCount = inCoinCount;
		}
	}
	
	public void setPopPrices(int[] inPopPrices) {
		popPrices = inPopPrices;
	}

	public boolean checkChangeLight(int[] inValidCoins, int[] inCoinCount) {
		setCoins(inValidCoins, inCoinCount);
		ArrayList<Integer> valuesOfChange = getPossibleChangeValues(validCoins, popPrices);
		for(int change : valuesOfChange) {
			// If at some point, we are NOT able to make change for a specific change value,
			// return false right away. (Since it means one instance is not covered by the machine.)
			if(!canMakeChange(change)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Determines what coins should be released as change after something calls it. (Post-buy, return, etc.)
	 * <p>
	 * Creates the list of (potentially redundant) coin denominations (Integer -> int) in descending order,
	 * assuming that the caller is able to interpret the list, and release coins for some machine accordingly.
	 * This method/module expects to be updated before, and after accordingly.
	 * 
	 * @param credit	The value to subtract from for each coin returned. Tries to get this to 0.
	 * @return			ArrayList containing a coin value denomination for each coin to be released.
	 */
	public ArrayList<Integer> makeChange(int credit) {
		int[] coinCountClone = coinCount.clone();
		ArrayList<Integer> returnList = new ArrayList<Integer>();
		qoinSort(0, validCoins.length -1);
		int i = validCoins.length - 1;
		
		// Loops through all coins in descending order, attempting to make change.
		while(i >= 0) {
			// Use current coin until no more are available, or current coin is too big.
			while(coinCountClone[i] > 0 && credit >= validCoins[i]) {
				credit -= validCoins[i];
				coinCountClone[i] --;
				returnList.add(validCoins[i]);
			}
            if(credit == 0) {
            	// Returns list of values that make credit == 0.
            	return returnList;
            }
            // Move to next coin. (Smaller than current coin.)
			i--;
		}
		// Returning as many values as possible. (Credit still in machine.)
		return returnList;
	}
	
	/**
	 * Using the currently available coins in the given machine, this algorithm attempts
	 * to make change with the coins it has. Assumes 'valid_Values' is in ascending order.
	 * 
	 * @param change		A particular value change value that the VM may have to make.
	 * @param validCoins	Currency values accepted by the given machine.
	 * @param coinCount		Number of coins for each corresponding currency value by the given machine.
	 * @return				true if the machine is able to make change. false otherwise.				
	 */
	private boolean canMakeChange(int change) {
		ArrayList<Integer> returnList = new ArrayList<Integer>(makeChange(change));
		while(!returnList.isEmpty()) {
			change -= returnList.get(0);
			returnList.remove(0);
		}
		if(change == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Determines what possible change values we may need to make.
	 * <p>
	 * Attempts to pay for every purchase with a single type of coin for all coins.
	 * Any remainders that cannot be paid are placed into a list of potential change values that
	 * will need to be made by the machine. (A 0 remainder means the machine can return some of the inserted coins).
	 * 
	 * @param validCoins	A list of potential coin denominations that could be used.
	 * @param popPrices		The price of each particular product, so that every value can be calculate for every pop.
	 * @return				Returns a list containing every cent value change the machine needs to be prepared to make.
	 */
	private ArrayList<Integer> getPossibleChangeValues(int[] validCoins, int[] popPrices) {
		ArrayList<Integer> changesToMake = new ArrayList<Integer>();
		int remainder;
		int loopCount;
		// For each coin on each type of pop, perform the test.
		for(int popPrice: popPrices) {
			for(int validCoinIndex: validCoins) {
				
				// Perform subtractions with a single type of coin until unable to.
				remainder = Math.abs(popPrice - validCoinIndex);
				loopCount = 1;
				while((validCoinIndex-1) < remainder) {
					remainder = Math.abs(remainder - validCoinIndex);
					loopCount ++;
				}
				// Debug print statements.
				System.out.printf("[%3d] Coin: %3d (%3d) times:  (Change/Remainder): %3d cents needs to be made.\n", popPrice, validCoinIndex, loopCount, remainder);
				if(remainder > 0 & !changesToMake.contains(remainder)) { // there is a remainder we need to add the ammount of change we need to give
					changesToMake.add(remainder);
				}
			}
		}
		return changesToMake;
	}
	
	// Performs a QuickSort algorithm on validCoins, but also carries similar index swaps on coinCount.
	// qoinSort and qoinPartition is an implementation of the QuickSort algorithm described in the textbook
	// 'Introduction to Algorithms Third Edition' by Cormen, Leiserson, Rivest, and Stein.
	private void qoinSort(int low, int high) {
		if (low < high) {
			int q = qoinPartition(low, high);
			qoinSort(low, q - 1);
			qoinSort(q + 1, high);
		}
	}
	
	// QuickSort dPartition, but also aligns a secondary array. (coinCount)
	private int qoinPartition(int low, int high) {
		int p = validCoins[high];
		int i = low;
		int j = high - 1;
		int temp;
		
			while(i <= j) {
				// Rightward sweep.
				while(i <= j && validCoins[i] <= p) {
					i++;
				}
				// Leftward sweep.
				while(j >= i && validCoins[j] >= p) {
					j--;
				}
				// Perform swap.
				if(i < j) {
					// Swap validCoins
					temp = validCoins[i];
					validCoins[i] = validCoins[j];
					validCoins[j] = temp;
					
					// Swap coinCount
					temp = coinCount[i];
					coinCount[i] = coinCount[j];
					coinCount[j] = temp;
				}
			}
		// Place Pivot (validCoins)
		temp = validCoins[i];
		validCoins[i] = validCoins[high];
		validCoins[high] = temp;
		
		// Place Pivot (coinCount)
		temp = coinCount[i];
		coinCount[i] = coinCount[high];
		coinCount[high] = temp;
		
		return i;
	}
}
