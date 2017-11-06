# SENG 300 - Group Assignment 2

### Revision 1 Requirements
Build on-top of given vending machine hardware. Do not alter the hardware.
Implement the following logic:

* ~~Customers must be able to press pop selection buttons and enter coins, in order to ultimately purchase pops.~~ [Completed in Assn1]

* ~~Again, Canadian coin denominations must be supported.~~ [Completed in Assn1]

* When the machine contains no credit for the user, a message "Hi there!" should be displayed for 5 seconds and then should be erased for 10 seconds, with this cycle repeating.

* When the user enters valid coins, the message "Credit: " and the amount of credit should be displayed.

* The machine should provide change once a pop is vended.  If the exact change can be provided, it should be, and the credit should go to zero.  If the exact change cannot be provided, the amount as close as possible to the exact change should be returned, but without going over; any remaining amount that cannot be returned should still exist as credit.

* Whenever exact change cannot be guaranteed for all possible transactions, the "exact change only" light should be turned on; otherwise, it should be off.

* If the machine cannot store additional coins or otherwise becomes aware of a problem that cannot be recovered from (including being out of all pop), the "out of order" light should be turned on; if the safety is enabled, this light should also be turned on; otherwise, it should be turned off.

* Each action of the user and the actions of the machine that are visible to the user should be logged in a text file (called the "event log").  You can determine the format of this text file.  Be aware that the vending machine possesses an internal clock, as per a standard Java virtual machine.

### Design Goals
In addition, design goals should allow for the ease of the following:
* easy extensibility to other forms of payment, including mixed modes (e.g., paying partially with coins, partially with credit card)
* ease of changing details on communication with the user and to the event log
* ease of supporting alternative hardware
(Note, do **not** implement other forms of payment for now. Your design however, should be able to support such a change.)
(Note, ease is defined as the addition of new code without any severe modification of the previous code. Rewriting the entire system is not considered 'easy')

### Deliverables
The following **six** items are to be provided for submission:
#### Team Submission:
* Source code that supports the above requirements and design goals.
* JUnit tests that 1) Unit test Logic classes, and 2) System test hardware with logic installed.
* One or more diagrams describing how the hardware and logic support user interactions.
* One page document justifying and design choices and how they meeting the given design goals.
* The log file of your team's Git repository.
#### Individual Submission:
* A peer evaluation document of you and your team's contribution to a solution.
... - Submitted in the form of an **excel** spreadsheet.
... - Based on an average, give a member a positive, neutral, or negative score.
... - Have the total of the scores SUM to 0.
... - Write a justification for each of the scores.

### Mr. Client's Comments
  "This description is good enough for you to get started, but we're working on certain changes to the hardware that will likely impact the requirements.  Be prepared to make adjustments to your design and implementation.  I'm not sure when our hardware changes will be ready to roll out for you.  We need to work out some issues with the vendors of some of the hardware components."

  "Any time you find any issues with the hardware, we'll have to fix it on our end.  Make sure you give us some time to achieve this.  I know: it's a moving target, but that can't be helped."

  "Oh, did I remember to tell you that the vending machine absolutely must be purple!  The marketing team will have my head if I  forget that."

### Grading Breakdown
* 20% - Models

* 20% - Completeness of Requirements

* 20% - Design

* 30% - Testing

* 10% - Individual Self-Peer Evaluations

### Extra Notes
* Expect there to potentially be a demo hosted during **class time.**
..* No late submissions are permitted.

### Information Gathering (Mr. Client & D2L)
