package ca.ucalgary.seng300.a2.exactChange;

import java.util.ArrayList;
import java.util.Iterator;

import org.lsmr.vending.hardware.VendingMachine;

public class ExactChange {
	
	private static int[] valid_coins = {1,5,10,25,100,200};
	private static int[] pop_prices = {125,150,170,200};
	private static int[] coin_count = {0,6,0,3,0,0};
	/*
	 * Call this to ask the vending machine to change the indicator light based on the state of the vm
	 */
	
	public static void main(String args[]) {
		ArrayList<Integer> valuesOfChange = ExactChange.getPossibleChangeValues(valid_coins, pop_prices);
		for(int change : valuesOfChange) {
			System.out.println(canMakeChange(change,valid_coins, coin_count));
		}
		System.out.println(valuesOfChange);
		
	}
	public static void manageExactChangeState(VendingMachine vm) {
		if(!canMakeChange(vm)) {
			//turn the light on
			vm.getExactChangeLight().activate();
		}else {
			vm.getExactChangeLight().deactivate();
		}
		
	}
	private static Boolean canMakeChange(VendingMachine vm) { // takes in vending machine
		
		//get the possible change values and loop though them returning false if there is a case where you cant make exact change 
		// and returns true if it can make change in all cases
		ArrayList<Integer> valuesOfChange = ExactChange.getPossibleChangeValues(valid_coins, pop_prices);
		for(int change : valuesOfChange) {
			if(!canMakeChange(change,valid_coins, coin_count)) return false;
		}
		return true;
	}
	
	
	//as long as all coins and pops share a common lcd (eg 5) the algorithem works and since we have a 5 then it will go through everyone that need i think
	public static ArrayList<Integer> getPossibleChangeValues(int[] possibleCoins, int[] possiblePrices) {
		// this function will return a list of all the possible values we will have to make change for
		//loop throught all the possible values for coins
		ArrayList<Integer> changesToMake = new ArrayList<Integer>();
		int v;
		int loopCount; // for debugging
		// loop through all the coin 
		for(int popPrice: possiblePrices) {
			for(int coinValue: possibleCoins) {
				//keep going until we cannot subtract anymore from the price
				 v = Math.abs(popPrice - coinValue); // make the first substract
				 loopCount = 1;
				while((coinValue-1) < v) {
					v = Math.abs(v - coinValue);
					loopCount ++;
				}
				//begin debug prints
				System.out.print(v);System.out.print(" + ");System.out.print(coinValue);System.out.print(" x ");
				System.out.print(loopCount);System.out.print(" = ");System.out.println(popPrice);
				//end debug prints 
				if(v > 0 & !changesToMake.contains(v)) { // there is a remainder we need to add the ammount of change we need to give
					changesToMake.add(v);
				}
			}
			
		}
		return changesToMake;
	}
	
	//will try to make change with the coins it has, assumes the valid values are in assending order
	public static Boolean canMakeChange(int change, int[] validValues, int[] coinCount) {
		//loop through the coins from decending values
		int i = validValues.length - 1;
		while(i > 0) {
			if(change >= validValues[i]) {
				while(coinCount[i] > 0 & change > 0) {
					change -= validValues[i];
					coinCount[i] --;
					System.out.println(change);
				}
			}
			i--;
		}
		if(change == 0) {
			return true;
		}
		
		
		return false;
	}
	
	
	
}



