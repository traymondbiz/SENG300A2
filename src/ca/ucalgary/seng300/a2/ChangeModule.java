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
	private static ChangeModule changeModule;
	private static VendingManager mgr;
	private static int[] validCoins;
	private static int[] coinCount;
	private static int[] popPrices;
	
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
	
	public static void setCoins(int[] inValidCoins, int[] inCoinCount) {
		if (inValidCoins.length == inCoinCount.length) {
			validCoins = inValidCoins;
			coinCount = inCoinCount;
		}
	}
	
	public static void setPopPrices(int[] inPopPrices) {
		popPrices = inPopPrices;
	}
	
	/*
	 * Debugging method that receives exact values (initialized above) and performs an
	 * algorithm to determine whether the 'Exact Change Light' should be on or off.
	 */
//	public static void main(String args[]) {
//		int[] inPopPrices = {50, 100, 150};
		
//		ArrayList<Integer> valuesOfChange = getPossibleChangeValues(validCoins, popPrices);
//		// Debugger message displaying all the potential values of change the machine needs to make.
//		System.out.printf("\n[Main] Values of change that needs to be made: %s\n\n", valuesOfChange);
//		for(int change : valuesOfChange) {
			//Debugger message determining whether each potential case of 
			//change that needs to be made is satisfied or not.
//			System.out.printf("[Main] Attempting to make change for %d cents:\n", change);
//			System.out.printf("[Info] Result: %b \n\n", canMakeChange(change, validCoins, coinCount));
//		}	
//	}
	public void updateExactChangeLigthState() {
		if(checkChangeLight(mgr.getValidCoinsArray(),mgr.getCount())){
			// we can make change deactivate the light
			mgr.acitvateExactChangeLight();
		}else {
			//we can make change so activate the light
			mgr.deactivateExactChangeLight();
		}
		
	}
	public boolean checkChangeLight( int[] validCoins, int[] coinCount) {
		ArrayList<Integer> valuesOfChange = getPossibleChangeValues(validCoins, popPrices);
		for(int change : valuesOfChange) {
			// If at some point, we are NOT able to make change for a specific
			if(!canMakeChange(change,validCoins, coinCount)) {
				return false;
			}
		}
		return true;
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
	private static boolean canMakeChange(int change, int[] validCoins, int[] coinCount) {
		
		/* Performs a Dual-Pivot Quicksort on 'valid_Values' in ascending order.
		 * See https://docs.oracle.com/javase/7/docs/api/java/util/Arrays.html#sort(int[])
		 * for more details. */
		
		// Needs reconsideration. Sorting does not sort the coinCount in the same way.
		// Implement a simple QuickSort that also aligns the coinCount list appropriately? Search online.
		// QuickSort, have it when DPartition swaps values for a particular list, it does so for the other list.
		// and, only returns the primary sorting list. (To be further partitioned. All lists are static in ChangeModule)

		qoinSort(0, validCoins.length -1);
		int[] numOfCoins = coinCount.clone();
		
		// Indexes start at 0.
		int i = validCoins.length - 1;
		
		while(i >= 0) {
			// In descending order from largest coin to smallest,
			// While that particular coin is still 'in-stock' in machine AND
			// there is still change that machine COULD return, perform the following:
			while(numOfCoins[i] > 0 && change >= validCoins[i] ) {
				
				// Remaining change that the machine must produce.
				System.out.printf("[Step] Change: %3d   ", change);
				// Particular coin being used to return currency.
				System.out.printf("Coin: %3d   ", validCoins[i]);
				// Remaining amount of particular coin in machine.
				System.out.printf("Left: %3d   ", numOfCoins[i]);
				
				// Logical operation to find remaining change to operate on.
				// Decrement the number of remaining particular coin in machine.
				change -= validCoins[i];
				numOfCoins[i] --;
				
				// Remaining change that the machine must produce.
				System.out.printf("Remainder: %3d\n", change);
				
			}
			
			// If no more change is needed to made, return true since
			// this amount of change to return is supported.
            if(change == 0) {
            	return true;
            }
            
            // Move to the next coin to perform operation.
			i--;
		}
		
		// If there are no ways the machine can return the
		// exact amount of change given its current resources,
		// return false. (And have another method enable the ExactChangeLight)
		return false;
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
	private static ArrayList<Integer> getPossibleChangeValues(int[] validCoins, int[] popPrices) {
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
	private static void qoinSort(int low, int high) {
		if (low < high) {
			int q = qoinPartition(low, high);
			qoinSort(low, q - 1);
			qoinSort(q + 1, high);
		}
	}
	
	// QuickSort dPartition, but also aligns a secondary array. (coinCount)
	private static int qoinPartition(int low, int high) {
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
	public ArrayList<Integer> getCoinsToReturn(int change, int[] validCoins, int[] coinCount) {
		
		ArrayList<Integer> return_list  = new ArrayList<Integer>();
		qoinSort(0, validCoins.length -1);
		int[] numOfCoins = coinCount.clone();
		
		// Indexes start at 0.
		int i = validCoins.length - 1;
		
		while(i >= 0) {
			// In descending order from largest coin to smallest,
			// While that particular coin is still 'in-stock' in machine AND
			// there is still change that machine COULD return, perform the following:
			while(numOfCoins[i] > 0 && change >= validCoins[i] ) {
				change -= validCoins[i];
				numOfCoins[i] --;
				return_list.add(validCoins[i]);
				// Remaining change that the machine must produce.
				System.out.printf("Remainder: %3d\n", change);
				
			}
			
            // Move to the next coin to perform operation.
			i--;
		}
		return return_list;
	}

	
}