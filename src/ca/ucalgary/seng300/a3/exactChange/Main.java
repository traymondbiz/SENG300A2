import java.util.ArrayList;
import java.util.Iterator;

public class Main {
	
	private static int[] valid_coins = {1,5,10,25,100,200};
	private static int[] pop_prices = {125,150,170,200};
	private static int[] coin_count = {0,100,0,0,0,0};
	
	public static void main(String args[]) {
		ArrayList<Integer> valuesOfChange = Main.getPossibleChangeValues(valid_coins, pop_prices);
		System.out.println(valuesOfChange);
		System.out.println(canMakeExactChange(valuesOfChange,valid_coins,coin_count));
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
				System.out.print(v);
				System.out.print(" + ");
				System.out.print(coinValue);
				System.out.print(" x ");
				System.out.print(loopCount);
				System.out.print(" = ");
				System.out.println(popPrice);
				//end debug prints 
				if(v > 0 & !changesToMake.contains(v)) { // there is a remainder we need to add the ammount of change we need to give
					changesToMake.add(v);
				}
			}
			
		}
		return changesToMake;
	}
	//again this algorithem work assuming they all have a common denominator of 5 which on canadian they should
	public static boolean canMakeExactChange(ArrayList<Integer> changesNeedToMake, int[] validValues, int[] coinStock) {
		//itterate through all the values in the array list since they are the changes we need to make 
		Iterator<Integer> changeValues = changesNeedToMake.iterator();
		int change;
	      while (changeValues.hasNext()) {
	    	  change = changeValues.next();
	         System.out.println(change);
	         if(!changePossible(change,validValues,coinStock)) { // cannot make change so return false
	        	 return false;
	         }
	      }
		return true; // since we did not return false we can assure the machine can make change
	}
	//will try to make change with the coins it has, assumes the valid values are in assending order
	public static boolean changePossible(int change, int[] coinValues,int[] coinCount) {
		System.out.println(change);
		int i; // keeps track of the coing array
		while(change > 0) {
			//find the coin value that is = or < so we can give change
			i = coinValues.length; 
			while(i > 0) {
				i--;
				if(coinValues[i] <= change) {
					System.out.println(i);
					break;
				}
				
			}
			//now subtract the coin value from change if it is there
			System.out.println("==================================================");
			System.out.println(coinValues.length);
			System.out.println(coinCount.length);
			System.out.println(coinValues[i]);
			if(coinCount[i] > 1) { //coin is ther so subtract it and remove one from the coin coun
				coinCount[i] --;
				change = change - coinValues[i];
			}else {//we dont have that coin so remove it from the array and make a new array
				int[] coinValuesNew = new int[coinValues.length -1];
				int[] coinCountNew = new int[coinCount.length - 1];
				System.out.println("removing:");
				System.out.println(coinValues[i]);
				for(int j=0;i<coinCountNew.length;i++) { // this for loop removes it from the array
					
						coinValuesNew[j] = coinValues[j];
						coinCountNew[j] = coinCount[j];
					
				}
				coinValues = coinValuesNew;
				coinCount = coinCountNew;
			}
			
			
		}
		if(change == 0) {
			return true;
		}
		return false;
	}
	
	
	
}
