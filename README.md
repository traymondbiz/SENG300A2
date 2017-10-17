# Assignment 1
## Task
* Implementing the logic of the vending machine, building on top of Rob's (simulated) hardware
  
## Requirements
* Get the vending machine to correctly react to coin insertions and button presses
* Write one or more classes that listen for the right kind of events from the hardware then call other methods on the hardware to get it to do the appropriate thing according to the current state
  * e.g. no money entered, no pop vended! 
* You MUST NOT alter the hardware source code. 
* Must handle the registration of logic classes with the appropriate hardware
* Mr. Client says don't: 
  * Be fancy at this point.  
  * Return change.  
  * Turn on notification lights.  
	* Print out meaningful messages to the VM's display.
  * If you ignore Mr. Client, expect him to not be happy. 
    * Food for thought: When is it OK to not worry about Mr. Client's happiness?

## Setup
* Class project name: ca.ucalgary.seng300.a1 containing a package of the same name
* JUnit package: ca.ucalgary.seng300.a1.test in that project.

	
## Testing
* Strive for good coverage of your logic implementation by your test suite.  
* You can assume that the hardware functions as it should
  * i.e., integration tests involving parts of your logic and my hardware are fine.

## Questions and Answers
* There are lots of things that have not been explained in much detail.  
  * Guessing at the answers is not a great idea, especially if you don't check.  
* Who can you check with?  Mr. Client, of course!  
  * Contact through D2L or in-person (during lecture)
  * Be professional.  Be courageous.  
  * "Dumb questions" are sometimes the most important ones to ask! 

## Assignment submission
* Submit to Dropbox folder by Oct 23 8AM
* Place project in a ZIP file
  * Do not include the hardware classes.
* Each team can just submit one version of your code.
* Add the names and IDs of all team members in comments


## Previously asked quesitons (Answers pending):
* How many coins does the maschine need to store?
* What are the denominations of coins?
* What is the currency?
* How many pop options are we offering?
* What is the maximum number of pop cans per machine/slot?
* When is a transaction completed?
* Would it be OK to not return invalid coins and store them in a separate coin rack?
* Do you want a demonstration of multiple buttons working, or just a proof of concept for one button?
* Is it possible to cancel a transaction?
* Since we’re not using indicator lights or display messages, what happens if you do not insert enough coins? What about if the pop you want is out? What about if it’s invalid input(e.g. You press a button that is not connected to any pop) ?
* What if a rack is out of pop?
