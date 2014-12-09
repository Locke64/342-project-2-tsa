import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import java.util.ArrayList;
import java.util.Iterator;

/*
 * This class represents a Security checkpoint. Each line will have a unique Security object that handles the traffic
 * moving through the line. When a piece of baggage or a passenger is received at the checkpoint, Security attempts
 * to fine the passenger of bag associated with the current message. If no associated object is present yet, the 
 * passenger or bag will wait in a list for the other object to arrive. When a pair is found, if either the baggage
 * or the passenger has failed the screening, the passenger will be taken to jail. Otherwise, the passenger will be
 * allowed to board their plane.
 *
 * @author Richard Jester
 */

public class Security extends VerboseActor {
	private ArrayList<VerboseMessage> passengers; // the collection of waiting passengers
	private ArrayList<VerboseMessage> baggage;    // the collection of waiting baggage
	private final ActorRef jail;                  // the reference for jail
	private int shutdown_count;                   // the count to determine when to shutdown Security

	/*
	 * The constructor for the Security object.
	 *
	 * @param  jail       the actor reference to the jail object
         * @param  line_num   the line number for this Security terminal
	 */
	
	public Security( ActorRef jail, int line_num ) {
		super( "Security" + line_num);
		this.jail = jail;
		passengers = new ArrayList<VerboseMessage>();
		baggage = new ArrayList<VerboseMessage>();
		shutdown_count = 2;
	}
	
	/*
	 * Method that receives incoming messages. Handles each type of possible message.
	 *
	 * @param  message  the current message received
	 */
	
	public void onReceive( Object message ) {
		VerboseMessage current_passenger = null;
		VerboseMessage current_bag = null;
		if( message instanceof Passenger ) {
			/**** handles incoming passengers *****/
			Passenger my_passenger = (Passenger) message;
			current_bag = checkWaitingBaggage(baggage, current_passenger);

			// if a corresponding bag is found, determine where the passenger will go
			// otherwise, place the passenger into the waiting collection
			if (current_bag != null) {
				baggage.remove(current_bag);
				if (current_bag instanceof FailedBaggage) {
					sendMessage(new FailedPassenger(my_passenger), jail, "Jail");
				} else {
					System.out.printf("%s passed the checkpoint. Bon voyage!\n",
						my_passenger.toString());
				}
			} else {
				passengers.add(my_passenger);
			}
		} else if( message instanceof FailedPassenger ) {
			/**** handles incoming failed passengers *****/
			FailedPassenger failure = (FailedPassenger) message;
			current_bag = checkWaitingBaggage(baggage, failure);

			// if a bag is found, remove it from collection and send the failed passenger to jail.
			// otherwise, add the failed passenger to the waiting collection
			if (current_bag != null) {
				baggage.remove(current_bag);
				sendMessage(failure, jail, "Jail");
			} else {
				passengers.add(failure);
			}
		} else if( message instanceof Baggage ) {
			/**** handles incoming baggage *****/
			Baggage bag = (Baggage) message;
			current_passenger = checkWaitingPassengers(passengers, bag);

			// if a matching passenger is found, determine where that passenger will go.
			// otherwise, add the baggage to the waiting baggage list.
			if (current_passenger != null) {
				passengers.remove(current_passenger);
				if (current_passenger instanceof Passenger) {
					System.out.printf("%s passed the checkpoint. Bon voyage!\n",
						current_passenger.toString());
				} else {
					sendMessage(current_passenger, jail, "Jail");
				}
			} else {
				baggage.add(bag);
			}
		} else if( message instanceof FailedBaggage ) {
			/**** handles incoming failed baggage *****/
			FailedBaggage failed_bag = (FailedBaggage) message;
			current_passenger = checkWaitingPassengers(passengers, failed_bag);

			// if a matching passenger is found, send that passenger to jail.
			// otherwise, add the failed bag to the waiting baggage.
			if (current_passenger != null) {
				passengers.remove(current_passenger);
				if (current_passenger instanceof Passenger) {
					sendMessage(new FailedPassenger((Passenger) current_passenger), jail, "Jail");
				} else {
					sendMessage(current_passenger, jail, "Jail");
				}
			}
		} else if(message instanceof Shutdown) {
			/**** handles the shutdown command *****/
			shutdown_count--;  //decremant the countdown counter

			// if the shutdown_counter is empty, then the BodyScanner and BagScanner objects
			// for this line are shutdown. Therefore, the Security object can begin shuting down
			// and pass the message to the Jail object.			
			if (shutdown_count == 0) {
				shutdown();
				shutdown(jail, "Jail");
			}
		} else {
			unhandled( message );
		}
	}

	/*
	 * This method is used to search the waiting baggage list for a matching bag for the current passenger
	 *
	 * @param bags     the collection of waiting bags
	 * @param pass     the current passenger at the checkpoint
	 *
	 * @return my_bag  the matching bag, or null if no bag was found
	 */

	private static VerboseMessage checkWaitingBaggage(ArrayList<VerboseMessage> bags, VerboseMessage pass) {
		Iterator<VerboseMessage> itr = bags.iterator();
		boolean owns_it = false;
		VerboseMessage my_bag = null;
		VerboseMessage temp_bag = null;

		// Check each waiting bag until a matching bag is found or all bags have been searched.
		while(itr.hasNext()) {
			temp_bag = itr.next();

			// if-else-if statement used to determine if the current bag being checked
			// is a regular Baggage object or a FaildBaggage object, then check accordingly
			if (temp_bag instanceof Baggage) {
				/**** handles a regular Baggage object *****/
				Baggage good_bag = (Baggage) temp_bag;
				if (pass instanceof Passenger) {
 					owns_it = ((Passenger) pass).owns(good_bag);
				} else {
					owns_it = ((FailedPassenger) pass).getPassenger().owns(good_bag);
				}

				if (owns_it) {
					my_bag = temp_bag;
					break;
				}
			} else if (temp_bag instanceof FailedBaggage) {
				/**** handles a FailedBaggage object *****/
				FailedBaggage failed_bag = (FailedBaggage) temp_bag;
				if (pass instanceof Passenger) {
					owns_it = ((Passenger) pass).owns(failed_bag.getBag());
				} else {
					owns_it = ((FailedPassenger) pass).getPassenger().owns(failed_bag.getBag());
				}

				if (owns_it) {
					my_bag = failed_bag;
					break;
				}
			}else {
				System.err.println("Found something other than baggage in the baggage list");
			}
		}
		return my_bag;
	}

	
	/*
	 * This method checks the waiting passenger collection for a matching passenger for the current passenger.
	 *
	 * @param passengers    the collection of passengers
	 * @param bag		the current bag
	 *
	 * @return		the owner of the bag, or null if no passenger was found
	 */

	private static VerboseMessage checkWaitingPassengers(ArrayList<VerboseMessage> passengers, VerboseMessage bag) {
		Iterator<VerboseMessage> itr = passengers.iterator();
		boolean owns_me = false;
		VerboseMessage owner = null;
		VerboseMessage temp_pass = null;

		// check every passenger waiting in the passenger collection until a matching
		// passenger is found or the entire collection was searched

		while(itr.hasNext()) {
			temp_pass = itr.next();

			// if-else-if determines if the current passenger has failed or not
			// and checks for matching accordingly.
			if (temp_pass instanceof Passenger) {
				/**** handles regular Passenger objects *****/
				Passenger good_pass = (Passenger) temp_pass;
				if (bag instanceof Baggage) {
					owns_me = good_pass.owns((Baggage) bag);
				} else {
					owns_me = good_pass.owns(((FailedBaggage) bag).getBag());
				}
				
				if (owns_me) {
					owner = good_pass;
					break;
				}
			} else if (temp_pass instanceof FailedPassenger) {
				/**** handles FailedPassenger objects *****/
				FailedPassenger failed_pass = (FailedPassenger) temp_pass;
				if (bag instanceof Baggage) {
					owns_me = failed_pass.getPassenger().owns((Baggage) bag);
				} else {
					owns_me = failed_pass.getPassenger().owns(((FailedBaggage) bag).getBag());
				}
				
				if (owns_me) {
					owner = temp_pass;
					break;
				}
			} else {
				System.err.println("Found something other than people in the list.");
			}
		}
		return owner;
	}
}
