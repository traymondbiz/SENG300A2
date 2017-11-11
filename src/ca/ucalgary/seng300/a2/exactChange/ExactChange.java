package ca.ucalgary.seng300.a2.exactChange;

import java.util.ArrayList;
import java.util.Arrays;

// Consider removing and performing purely logic.
import org.lsmr.vending.hardware.VendingMachine;

/**
 * Software Engineering 300 - Group Assignment 2
 * ExactChange.java
 * 
 * An algorithm that determines how excess change a given vending machine would
 * potentially have to return. This is output in the form of a list.
 * For example, a list [5, 25, 30, 50] would mean the Vending Machine has to be able
 * to return 5, 25, 30, and 50 cents on a given transaction. If one of these is not
 * possible, the exact change light is turned on.
 * 
 * Furthermore, due to the logic relying on mathematics, any currency-pop combination.
 * is compatible provided that the appropriate lists are sent of ExactChange to be calculated on.
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
 * // Assignment 1
 * @author Thomas Coderre 			(10169277)
 * @author Thobthai Chulpongsatorn 	(30005238)
 * 
 * @version	2.0
 * @since	2.0
 */
public class ExactChange {

 	/* 
	 * Initialized arrays representing arbitrary values to calculate on.
	 * valid_coins	represents the machine's accepted currency denominations.
	 * pop_prices	represents the costs of some particular pop. (Dependent on index.)
	 * coin_count	represents the current available amount of coins (excluding the user's)
	 * 				that can be used to make change. Ascending value order.
	 */
	private static int[] validCoins = {1, 5, 10, 25, 100, 200};
	private static int[] coinCount 	= {0, 1, 0, 3, 0, 0};
	private static int[] popPrices 	= {125, 150, 170, 200};
	
	/*
	 * Debugging method that receives exact values (initialized above) and performs an
	 * algorithm to determine whether the 'Exact Change Light' should be on or off.
	 */
	public static void main(String args[]) {
		ArrayList<Integer> valuesOfChange = getPossibleChangeValues(validCoins, popPrices);
		// Debugger message displaying all the potential values of change the machine needs to make.
		System.out.printf("[Main] Values of change that needs to be made: %s\n", valuesOfChange);
		for(int change : valuesOfChange) {
			//Debugger message determining whether each potential case of 
			//change that needs to be made is satisfied or not.
			System.out.printf("[Main] Attempting to make change for %d cents:\n", change);
			System.out.printf("[Info] Result: %b \n\n", canMakeChange(change, validCoins, coinCount));
		}	
	}

	/*
	 * Primary method to be called by other logical components (after a purchase/return)
	 * It receives some VendingMachine, collects its mathematical values and
	 * sends it to the algorithm, and finally activates/deactivates the
	 * 'Exact Change Light' according to the result.
	 */
	public static void refreshExactChangeLight(VendingMachine vm) {
		boolean exactChangeFlag;
		if(!canMakeChange(vm)) {
			vm.getExactChangeLight().activate();
		}
		else {
			vm.getExactChangeLight().deactivate();
		}
		
	}
	
	private static boolean canMakeChange(VendingMachine vm) { // takes in vending machine
		
		//get the possible change values and loop though them returning false if there is a case where you cant make exact change 
		// and returns true if it can make change in all cases
		
		//TODO 
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
	 * @param validValues	Currency values accepted by the given machine.
	 * @param coinCount		Number of coins for each corresponding currency value by the given machine.
	 * @return				true if the machine is able to make change. false otherwise.				
	 */
	private static boolean canMakeChange(int change, int[] validValues, int[] coinCount) {
		
		/* Performs a Dual-Pivot Quicksort on 'valid_Values' in ascending order.
		 * See https://docs.oracle.com/javase/7/docs/api/java/util/Arrays.html#sort(int[])
		 * for more details. */
		Arrays.sort(validValues);
		
		/* Temporary variable numOfCoins is made so that the algorithm
		 * can simulate coin returns and the remaining number of a particular coin
		 * without actually modifying any values. */
		//TODO Determine whether the existing means of copying the array should be used or not.
		//int[] numOfCoins = new int[coinCount.length];
		//for(int i = 0; i < coinCount.length; i++) {
		//	numOfCoins[i] = coinCount[i];
		//}
		
		int[] numOfCoins = coinCount.clone();
		
		// Indexes start at 0.
		int i = validValues.length - 1;
		
		while(i >= 0) {
			// In descending order from largest coin to smallest,
			// While that particular coin is still 'in-stock' in machine AND
			// there is still change that machine COULD return, perform the following:
			while(numOfCoins[i] > 0 && change >= validValues[i] ) {
				
				// Remaining change that the machine must produce.
				System.out.printf("[Step] Change: %3d   ", change);
				// Particular coin being used to return currency.
				System.out.printf("Coin: %3d   ", validValues[i]);
				// Remaining amount of particular coin in machine.
				System.out.printf("Left: %3d   ", numOfCoins[i]);
				
				// Logical operation to find remaining change to operate on.
				// Decrement the number of remaining particular coin in machine.
				change -= validValues[i];
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
	
	
	//as long as all coins and pops share a common lcd (eg 5) the algorithem works and since we have a 5 then it will go through everyone that need i think
	private static ArrayList<Integer> getPossibleChangeValues(int[] validCoins, int[] popPrices) {
		// this function will return a list of all the possible values we will have to make change for
		//loop throught all the possible values for coins
		ArrayList<Integer> changesToMake = new ArrayList<Integer>();
		int v;
		int loopCount; // for debugging
		// loop through all the coin 
		for(int popPrice: popPrices) {
			for(int validCoinIndex: validCoins) {
				//keep going until we cannot subtract anymore from the price
				 v = Math.abs(popPrice - validCoinIndex); // make the first substract
				 loopCount = 1;
				while((validCoinIndex-1) < v) {
					v = Math.abs(v - validCoinIndex);
					loopCount ++;
				}
				//begin debug prints
				System.out.print(v);System.out.print(" + ");System.out.print(validCoinIndex);System.out.print(" x ");
				System.out.print(loopCount);System.out.print(" = ");System.out.println(popPrice);
				//end debug prints 
				if(v > 0 & !changesToMake.contains(v)) { // there is a remainder we need to add the ammount of change we need to give
					changesToMake.add(v);
				}
			}
		}
		return changesToMake;
	}	
}